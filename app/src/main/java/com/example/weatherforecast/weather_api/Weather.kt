package com.example.weatherforecast.weather_api

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)