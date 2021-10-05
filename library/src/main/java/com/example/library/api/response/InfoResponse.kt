package com.example.library.api.response

import com.example.library.data.PlaceInfo

data class InfoResponse(
    val result: PlaceInfo,
    val status : String
)