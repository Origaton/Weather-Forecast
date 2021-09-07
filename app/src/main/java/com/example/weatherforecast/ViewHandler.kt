package com.example.weatherforecast

import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.weather_api.WeatherInfo

class ViewHandler(private val bindingClass: ActivityMainBinding) : ViewHandlerInterface {

    override fun storage(locationInfo: LocationInfo, weatherInfo: WeatherInfo?) {
        showCity(locationInfo.cityName)
        if (weatherInfo != null) {
            showTemperature(weatherInfo.current.temp)
        }
    }

    override fun showCity(city: String) {
        //bindingClass.currentCity.text = city
    }

    override fun showTemperature(temperature: Double) {
        bindingClass.currentTemperature.text = temperature.toString()
    }

}