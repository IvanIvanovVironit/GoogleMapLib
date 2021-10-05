package com.example.library.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceInfo(
//    val address_components : List<Address_components>,
    val adr_address : String,
    val business_status : String,
    val formatted_address : String,
    val formatted_phone_number : String,
//    val geometry : Geometry,
    val icon : String,
    val icon_background_color : String,
    val icon_mask_base_uri : String,
    val international_phone_number : String,
    val name : String,
    val opening_hours : Opening_hours,
    val photos : List<Photos>,
    val place_id : String,
//    val plus_code : Plus_code,
    val rating : Double,
    val reference : String,
    val reviews : List<Reviews>,
    val types : List<String>,
    val url : String,
    val user_ratings_total : Int,
    val utc_offset : Int,
    val vicinity : String,
    val website : String
    ) : Parcelable{

        @Parcelize
        data class Opening_hours (
            val open_now : Boolean,
            val periods : List<Periods>,
            val weekday_text : List<String>
        ) : Parcelable

            @Parcelize
            data class Periods (
                val close : Close,
                val open : Open
            ) : Parcelable

                @Parcelize
                data class Close (
                    val day : Int,
                    val time : Int
                ): Parcelable

                @Parcelize
                data class Open (
                    val day : Int,
                    val time : Int
                ): Parcelable

        @Parcelize
        data class Photos (
            val height : Int,
            val html_attributions : List<String>,
            val photo_reference : String,
            val width : Int
        ): Parcelable

        @Parcelize
        data class Reviews (
            val author_name : String,
            val author_url : String,
            val language : String,
            val profile_photo_url : String,
            val rating : Int,
            val relative_time_description : String,
            val text : String,
            val time : Int
        ) : Parcelable
    }
