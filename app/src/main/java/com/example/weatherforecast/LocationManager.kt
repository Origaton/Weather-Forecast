package com.example.weatherforecast

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*

class LocationManager(private var context: Context) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationInfo: LocationInfo
    private var isSuccess: Boolean = false

    suspend fun setLocation(defaultConfiguration: Boolean): LocationInfo = coroutineScope {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        do {
            getCurrentLocation()
        } while (!isSuccess)
        return@coroutineScope when (defaultConfiguration) {
            false -> locationInfo
            else -> setDefaultLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation() = coroutineScope {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            locationInfo = LocationInfo(
                cityName = Geocoder(context).getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                ).first().subLocality,
                latitude = location.latitude,
                longitude = location.longitude
            )
            isSuccess = true
        }.addOnFailureListener {
            locationInfo = LocationInfo(DEFAULT_CITY, DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
            isSuccess = true
        }
    }

    private suspend fun setDefaultLocation(): LocationInfo = coroutineScope {
        return@coroutineScope LocationInfo(DEFAULT_CITY, DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    }

    companion object {
        private const val DEFAULT_CITY = "Москва"
        private const val DEFAULT_LATITUDE = 55.7522
        private const val DEFAULT_LONGITUDE = 37.6156
    }
}
