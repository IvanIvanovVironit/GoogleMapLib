package com.example.library.api

import com.example.library.api.response.InfoResponse
import com.example.library.api.response.RouteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapApi {

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
    }

    @GET("maps/api/place/details/json")
    suspend fun getInfo(
        @Query("place_id") placeId: String,
        @Query("key") key: String
    ): InfoResponse

    @GET("maps/api/directions/json")
    suspend fun getRoutes(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): RouteResponse
}