package com.example.weatherforecast

import android.widget.ImageView
import android.widget.TextView
import com.example.weatherforecast.weather_api.WeatherInfo

interface ViewHandlerInterface {

    fun storage(locationInfo: LocationInfo, weatherInfo: WeatherInfo?)

    fun showCurrentWeather(locationInfo: LocationInfo, weatherInfo: WeatherInfo?)

    fun showForecast(weatherInfo: WeatherInfo?)

    fun showHourlyForecast(weatherInfo: WeatherInfo)

    fun showDailyForecast(weatherInfo: WeatherInfo)

    fun showCity(city: String)

    fun showCurrentTemperature(temp: String, feelsLike: String)

    fun showWeatherParameters(
        description: String,
        sunrise: String,
        sunset: String,
        windSpeed: String,
        humidity: String,
        pressure: String,
        cloudiness: String
    )

    fun showLastUpdateTime(dt: String)

    fun setDailyWeather(
        textView: TextView,
        imageView: ImageView,
        textViewTemp: TextView,
        weatherInfo: WeatherInfo,
        day: Int
    )

    fun showWeatherImage(id: Char, imageView: ImageView)

    fun showLoading()
}