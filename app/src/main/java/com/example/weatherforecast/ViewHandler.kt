package com.example.weatherforecast

import com.example.weatherforecast.databinding.ActivityMainBinding

class ViewHandler(private val bindingClass: ActivityMainBinding) : ViewHandlerInterface {

    override fun realiseButton(locationInfo: LocationInfo) {
        bindingClass.button.setOnClickListener {
            showCity(
                locationInfo.cityName,
                locationInfo.latitude.toString(),
                locationInfo.longitude.toString()
            )
        }
    }

    override fun showCity(city: String, latitude: String, longitude: String) {
        bindingClass.currentCity.text = city
        bindingClass.latitudeAndLongitude.text = latitude
        bindingClass.currentTemperature.text = longitude
    }

    override fun showTemperature(temperature: String) {
        bindingClass.currentTemperature.text = temperature
    }

}