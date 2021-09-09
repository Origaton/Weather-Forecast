package com.example.weatherforecast

import com.example.weatherforecast.weather_api.WeatherInfo
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherManager(private val locationInfo: LocationInfo) {

    private val appid: String = "ea420f80f3ce89a6e2d374952f12b8a6"

    suspend fun getCurrentLocationData(): WeatherInfo = coroutineScope {
        val retrofit = Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ApiRequests::class.java)

        val response = async(Dispatchers.IO) {
            api.getWeatherInfo(locationInfo.latitude!!, locationInfo.longitude!!, appid = appid)
        }
        return@coroutineScope response.await()
    }
}