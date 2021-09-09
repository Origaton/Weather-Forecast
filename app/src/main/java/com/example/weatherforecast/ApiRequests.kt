package com.example.weatherforecast

import com.example.weatherforecast.location_api.CustomLocationInfo
import com.example.weatherforecast.weather_api.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/onecall?lat=55.7522&lon=37.6156&exclude=minutely,alerts&units=metric&lang=ru&appid=ea420f80f3ce89a6e2d374952f12b8a6
//http://api.openweathermap.org/geo/1.0/direct?q=Москва&limit=1&appid=ea420f80f3ce89a6e2d374952f12b8a6
const val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/"
const val LOCATION_URL: String = "http://api.openweathermap.org/geo/1.0/"

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

    @GET("direct")
    suspend fun getCustomCoordinates(
        @Query("q") q: String,
        @Query("limit") limit: Int,
        @Query("appid") appid: String
    ): CustomLocationInfo

}