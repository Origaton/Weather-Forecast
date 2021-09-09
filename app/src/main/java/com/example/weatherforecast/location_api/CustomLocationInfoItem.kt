package com.example.weatherforecast.location_api

data class CustomLocationInfoItem(
    val country: String,
    val lat: Double?,
    val local_names: LocalNames,
    val lon: Double?,
    val name: String
)