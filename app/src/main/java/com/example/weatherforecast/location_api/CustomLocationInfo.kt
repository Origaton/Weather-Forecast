package com.example.weatherforecast.location_api

data class CustomLocationInfo(
    val info: Info,
    val options: Options,
    val results: List<Result>
)