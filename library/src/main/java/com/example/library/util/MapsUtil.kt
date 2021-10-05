package com.example.library.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.library.api.GoogleMapApi
import com.example.library.common.Result
import com.example.library.data.GoogleMapRepository
import com.example.library.data.PlaceInfo
import com.example.library.data.Routes
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.coroutineScope
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

class MapsUtil {

    private var repository: GoogleMapRepository? = null

    companion object {
        private const val DEFAULT_ZOOM = 15
        const val TAG = "TAG"
    }

    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var lastKnownLocation: Location? = null
    private var mapType = true
    private var markerPlace: Marker? = null

    private var distance: String? = null
    private var duration: String? = null
    private var endLocation: String? = null
    private var startLocation: String? = null
    private var travelMode: String? = null

//    private var placeIdOrigin: String? = null
//    private var placeIdDirection: String? = null
    private var markerOrigin: Marker? = null
    private var markerDirection: Marker? = null
    private var polyline: Polyline? = null
    private var latLngMarker: LatLng? = null


    fun initializeMap(context: Context, key: String){
        Places.initialize(context, key)
        placesClient = Places.createClient(context)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
    }


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
    fun getDeviceLocation(locationPermissionGranted: Boolean, activity: Activity,
        context: Context, map: GoogleMap) {
        try {
            if (locationPermissionGranted) {
                val locationResult = LocationServices.getFusedLocationProviderClient(context).lastLocation
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
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    suspend fun getRoute(context: Context, placeIdOrigin: String, placeIdDirection: String): List<LatLng> {
        var listLatLng: List<LatLng> = emptyList()
        var route: Routes? = null
        if (placeIdOrigin == null || placeIdDirection == null) {
            Toast.makeText(context, "Enter locations!", Toast.LENGTH_SHORT).show()
        } else {
            try {
                Log.d(
                    TAG,
                    "place_id:$placeIdOrigin\", \"place_id:$placeIdDirection"
                )
                route = getRoutes(
                    "place_id:$placeIdOrigin",
                    "place_id:$placeIdDirection"
                )!![0]
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
            if (route != null) {
                listLatLng = PolyUtil.decode(route.overviewPolyline.points)
//                                mGoogleMapViewModel.setPolyline(listLatLng)
            }
        }
        return listLatLng
    }

    suspend fun getRoutes (origin: String, destination: String): List<Routes>? {

        val provideRetrofitLib: Retrofit =
        Retrofit.Builder()
            .baseUrl(GoogleMapApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        repository = GoogleMapRepository(provideGoogleApiLib(provideRetrofitLib))

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

    fun provideGoogleApiLib(retrofit: Retrofit): GoogleMapApi =
        retrofit.create(GoogleMapApi::class.java)

    fun geoLocate(searchString: String, typeMarker: Int, context: Context) {
        Log.d(TAG, "geoLocate: geolocating")
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
            if (typeMarker == 0) {
                val markerOptions = MarkerOptions()
                    .title(address.getAddressLine(0))
                    .position(latLng)
                    .snippet(address.countryName)

//                mGoogleMapViewModel.setMarkerOrigin(markerOptions)
            }
            if (typeMarker == 1) {
                val markerOptions = MarkerOptions()
                    .title(address.getAddressLine(0))
                    .position(latLng)
                    .snippet(address.countryName)

//                mGoogleMapViewModel.setMarkerDirection(markerOptions)
            }
        }
    }

//    fun getInfo(): PlaceInfo =


    fun onPolylineClick(poliline: Polyline, context: Context) {
        Toast.makeText(
            context,
            "Travel Mode: $travelMode\nDuration = $duration\nDistance = $distance" +
                    "\nStart Location: $startLocation\nEnd Location: $endLocation",
            Toast.LENGTH_SHORT
        ).show()
    }


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