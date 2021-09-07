package com.example.weatherforecast

import com.example.weatherforecast.weather_api.WeatherInfo

interface ViewHandlerInterface {

    fun storage(locationInfo: LocationInfo, weatherInfo: WeatherInfo?)

    fun showCity(city: String)

    fun showTemperature(temperature: Double)

}