package com.example.weatherforecast

import com.example.weatherforecast.location_api.CustomLocationInfo
import com.example.weatherforecast.weather_api.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/onecall?lat=55.7522&lon=37.6156&exclude=minutely,alerts&units=metric&lang=ru&appid=ea420f80f3ce89a6e2d374952f12b8a6
//http://open.mapquestapi.com/geocoding/v1/address?key=meY8hgyVw6l18Z0uuXbXaA9Y6HrUenAL&location=Москва&outFormat=json
const val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/"
const val LOCATION_URL: String = "http://open.mapquestapi.com/geocoding/v1/"

interface ApiRequests {
    @GET("onecall")
    suspend fun getWeatherInfo(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
        @Query("appid") appid: String
    ): WeatherInfo

    @GET("address")
    suspend fun getCustomCoordinates(
        @Query("key") key: String,
        @Query("location") location: String,
        @Query("outFormat") outFormat: String = "json"
    ): CustomLocationInfo

}