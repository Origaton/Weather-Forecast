package com.example.weatherforecast

import com.example.weatherforecast.weather_api.WeatherInfo

interface ViewHandlerInterface {

    fun storage(locationInfo: LocationInfo, weatherInfo: WeatherInfo?)

    fun showCity(city: String)

    fun showCurrentTemperature(temp: String, feelsLike: String)

    fun showWeatherParameters(
        description: String,
        sunrise: String,
        sunset: String,
        windSpeed: String,
        humidity: String,
        pressure: String
    )
}