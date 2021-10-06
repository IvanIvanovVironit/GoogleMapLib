package com.example.library.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Routes(
    val bounds : Bounds,
    val copyrights : String,
    val legs : List<Legs>,
    @SerializedName("overview_polyline")
    val overviewPolyline : OverviewPolyline,
    val summary : String,
    val warnings : List<String>,
    @SerializedName("waypoint_order")
    val waypointOrder : List<String>
    ) : Parcelable {

        @Parcelize
        data class Bounds (
            val northeast : Northeast,
            val southwest : Southwest
        ) : Parcelable

            @Parcelize
            data class Northeast (
                val lat : Double,
                val lng : Double
            ): Parcelable

            @Parcelize
            data class Southwest (
                val lat : Double,
                val lng : Double
            ): Parcelable


        @Parcelize
        data class Legs (
            val distance : Distance,
            val duration : Duration,
            @SerializedName("end_address")
            val endAddress : String,
            @SerializedName("end_location")
            val endLocation : EndLocation,
            @SerializedName("start_address")
            val startAddress : String,
            @SerializedName("start_location")
            val startLocation : StartLocation,
            val steps : List<Steps>,
            @SerializedName("traffic_speed_entry")
            val trafficSpeedEntry : List<String>,
            @SerializedName("via_waypoint")
            val viaWaypoint : List<String>
        ): Parcelable

            @Parcelize
            data class Distance (
                val text : String,
                val value : Int
            ): Parcelable

            @Parcelize
            data class Duration (
                val text : String,
                val value : Int
            ): Parcelable

            @Parcelize
            data class EndLocation (
                val lat : Double,
                val lng : Double
            ): Parcelable

            @Parcelize
            data class StartLocation (
                val lat : Double,
                val lng : Double
            ): Parcelable

            @Parcelize
            data class Steps (
                val distance : Distance,
                val duration : Duration,
                @SerializedName("end_location")
                val endLocation : EndLocation,
                @SerializedName("html_instructions")
                val htmlInstructions : String,
                val polyline : Polyline,
                @SerializedName("start_location")
                val startLocation : StartLocation,
                @SerializedName("travel_mode")
                val travelMode : String
            ): Parcelable

                @Parcelize
                data class Polyline (
                    val points : String
                ): Parcelable


    @Parcelize
        data class OverviewPolyline (
            val points : String
        ): Parcelable
    }
