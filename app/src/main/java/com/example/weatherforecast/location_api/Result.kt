package com.example.weatherforecast.location_api

data class Result(
    val locations: List<Location>,
    val providedLocation: ProvidedLocation
)