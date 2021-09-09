package com.example.weatherforecast

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationManager(private var context: Context) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationInfo: LocationInfo
    private var isSuccess: Boolean = false
    private val appid: String = "ea420f80f3ce89a6e2d374952f12b8a6"

    suspend fun setCurrentLocation(defaultConfiguration: Boolean): LocationInfo = coroutineScope {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (!defaultConfiguration) {
            do {
                getCurrentLocation()
            } while (!isSuccess)
        }
        return@coroutineScope when (defaultConfiguration) {
            false -> locationInfo
            true -> setDefaultLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation() = coroutineScope {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            locationInfo = LocationInfo(
                Geocoder(context).getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                ).first().locality,
                location.latitude,
                location.longitude
            )
            isSuccess = true
        }.addOnFailureListener {
            Toast.makeText(
                context,
                "Ошибка при получении местоположения. Установленно местоположение по умолчанию",
                Toast.LENGTH_SHORT
            ).show()
            locationInfo = LocationInfo(DEFAULT_CITY, DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
            isSuccess = true

        }
    }

    private suspend fun setDefaultLocation(): LocationInfo = coroutineScope {
        return@coroutineScope LocationInfo(DEFAULT_CITY, DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    }

    suspend fun setCustomLocation(city: String): LocationInfo = coroutineScope {
        val retrofit = Retrofit.Builder()
            .baseUrl(LOCATION_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ApiRequests::class.java)

        val response = async(Dispatchers.IO) {
            api.getCustomCoordinates(city, 1, appid)
        }
        val customLocation = response.await()
        return@coroutineScope when (customLocation.isEmpty()) {
            true -> LocationInfo(null, null, null)
            false -> LocationInfo(
                cityName = city,
                latitude = customLocation[0].lat!!,
                longitude = customLocation[0].lon!!
            )
        }
    }

    companion object {
        private const val DEFAULT_CITY = "Москва"
        private const val DEFAULT_LATITUDE = 55.7522
        private const val DEFAULT_LONGITUDE = 37.6156
    }
}
