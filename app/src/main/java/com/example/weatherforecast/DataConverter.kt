package com.example.weatherforecast

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

class DataConverter {

    @SuppressLint("SimpleDateFormat")
    fun convertCurrentTime(dt: Double, sunRise: Double, sunSet: Double): List<String> {
        val dataFormat = SimpleDateFormat("HH:mm")
        val newTime = dataFormat.format(dt * 1000)
        val newSunRise = dataFormat.format(sunRise * 1000)
        val newSunSet = dataFormat.format(sunSet * 1000)
        return listOf<String>(newTime, newSunRise, newSunSet)
    }

    fun hPaConvertToMmHg(pressure: Double): String {
        val newPressure = pressure * 0.7501
        return newPressure.toInt().toString()
    }
}