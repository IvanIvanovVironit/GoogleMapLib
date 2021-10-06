package com.example.library.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.example.library.api.GoogleMapApi
import com.example.library.common.Result
import com.example.library.data.GoogleMapRepository
import com.example.library.data.Routes
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MapsUtil {

    private var repository: GoogleMapRepository? = null

    companion object {
        private const val DEFAULT_ZOOM = 15
        const val TAG = "TAG"
    }

    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var lastKnownLocation: Location? = null
    private var mapType = true
    private var markerPlace: Marker? = null
    private var distance: String? = null
    private var duration: String? = null
    private var endLocation: String? = null
    private var startLocation: String? = null
    private var travelMode: String? = null
    private var polyline: Polyline? = null
    private var latLngMarker: LatLng? = null


    private fun handleTouchEvent() {
        map.apply {
            setOnPoiClickListener {
//                mGoogleMapViewModel.setPlaceId(it.placeId)
//                Log.d(TAG, "PlaceId: ${it.placeId} ${it.name} ${it.latLng}")
//                showMarkerInfo()
            }
            setOnMapLongClickListener {
                latLngMarker = it
                if (markerPlace == null) {
                    markerPlace = map.addMarker(
                        MarkerOptions()
                            .title("${it.latitude},${it.longitude}")
                            .position(it)
                    )
                } else {
                    markerPlace?.remove()
                    markerPlace = map.addMarker(
                        MarkerOptions()
                            .title("${it.latitude},${it.longitude}")
                            .position(it)
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        locationPermissionGranted: Boolean, activity: Activity,
        context: Context, map: GoogleMap, defaultLocation: LatLng, DEFAULT_ZOOM: Float
    ) {
        try {
            if (locationPermissionGranted) {
                val locationResult =
                    LocationServices.getFusedLocationProviderClient(context).lastLocation
                locationResult.addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM)
                        )
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    suspend fun getRoute(placeIdOrigin: String, placeIdDirection: String, KEY: String): Routes {
        var route: Routes? = null
        try {
            Log.d(
                TAG,
                "place_id:$placeIdOrigin\", \"place_id:$placeIdDirection"
            )
            route = getRoutes(
                "place_id:$placeIdOrigin",
                "place_id:$placeIdDirection",
                KEY
            )!![0]
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
        return route!!
    }

    suspend fun getRoutes(origin: String, destination: String, KEY: String): List<Routes>? {
        val provideRetrofitLib: Retrofit =
            Retrofit.Builder()
                .baseUrl(GoogleMapApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        repository = GoogleMapRepository(provideGoogleApiLib(provideRetrofitLib), KEY)

        when (val result = repository!!.getRoutes(origin, destination)) {
            is Result.Success -> {
                return result.value
            }
            is Result.Failure -> {
                val exception = result.throwable.localizedMessage
                Log.e(TAG, "Error! GoogleMapViewModel.getRoutes is Failure \n$exception")
                return null
            }
        }
    }

    private fun provideGoogleApiLib(retrofit: Retrofit): GoogleMapApi =
        retrofit.create(GoogleMapApi::class.java)

    fun geoLocate(
        searchString: String,
        typeMarker: Int,
        context: Context,
        map: GoogleMap
    ): MarkerOptions {
        Log.d(TAG, "geoLocate: geolocating")
        var markerOptions: MarkerOptions? = null
        val geocoder = Geocoder(context)
        var list: List<Address> = ArrayList()
        try {
            list = geocoder.getFromLocationName(searchString, 1)
        } catch (e: IOException) {
            Log.e(TAG, "geoLocate: IOException: " + e.message)
        }
        if (list.size > 0) {
            val address: Address = list[0]
            Log.d(TAG, "geoLocate: found a location: " + address.getAddressLine(0).toString())

            val latLng = LatLng(address.latitude, address.longitude)
            map.moveCamera(
                CameraUpdateFactory
                    .newLatLngZoom(latLng, DEFAULT_ZOOM.toFloat())
            )
            markerOptions = MarkerOptions()
                .title(address.getAddressLine(0))
                .position(latLng)
                .snippet(address.countryName)
        }
        return markerOptions!!
    }


//    fun onPolylineClick(poliline: Polyline, context: Context) {
//        Toast.makeText(
//            context,
//            "Travel Mode: $travelMode\nDuration = $duration\nDistance = $distance" +
//                    "\nStart Location: $startLocation\nEnd Location: $endLocation",
//            Toast.LENGTH_SHORT
//        ).show()
//    }


    fun onAutoComplete(fragment: AutocompleteSupportFragment) {
        fragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        fragment.setCountries("AU", "NZ", "USA", "BLR", "RUS")
    }

    fun getPolylineOptions(): PolylineOptions {
        val options = PolylineOptions()
        options.color(Color.BLUE)
        options.width(15f)
        options.clickable(true)
        return options
    }
}