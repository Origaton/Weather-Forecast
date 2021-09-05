package com.example.weatherforecast

interface ViewHandlerInterface {

    fun realiseButton(locationInfo: LocationInfo)

    fun showCity(city: String, latitude: String, longitude: String)

    fun showTemperature(temperature: String)

}