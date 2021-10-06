package com.example.library.data

import com.google.gson.annotations.SerializedName

data class GeocodedWaypoints(
    @SerializedName("geocoder_status")
    val geocoderStatus : String,
    @SerializedName("place_id")
    val placeId : String,
    val types : List<String>
)
