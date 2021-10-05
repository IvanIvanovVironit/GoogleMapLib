package com.example.library.api.response

import com.example.library.data.Routes

data class RouteResponse(
    val routes: List<Routes>,
    val status : String
)