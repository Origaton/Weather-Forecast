package com.example.weatherforecast

import com.example.weatherforecast.databinding.ActivityMainBinding

class ViewHandler(private val bindingClass: ActivityMainBinding) : ViewHandlerInterface {

    override fun showCity(city: String) {
        bindingClass.currentCity.text = city
    }

    override fun showTemperature(temperature: String) {
        bindingClass.currentTemperature.text = temperature
    }

}