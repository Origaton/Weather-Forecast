package com.example.weatherforecast

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationManager(private var context: Context) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationInfo: LocationInfo
    private var isSuccess: Boolean = false
    private val key: String = "meY8hgyVw6l18Z0uuXbXaA9Y6HrUenAL"

    suspend fun setCurrentLocation(defaultConfiguration: Boolean): LocationInfo = coroutineScope {
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

    suspend fun setCustomLocation(city: String): LocationInfo = coroutineScope {
        val retrofit = Retrofit.Builder()
            .baseUrl(LOCATION_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ApiRequests::class.java)

        Log.d("location_test","$city")
        val response = async(Dispatchers.IO) {
            api.getCustomCoordinates(key, city)
        }
        val customLocation = response.await()
        return@coroutineScope LocationInfo(
            cityName = city,
            latitude = customLocation.results.component1().locations.component1().latLng.lat,
            longitude = customLocation.results.component1().locations.component1().latLng.lng
        )
    }

    companion object {
        private const val DEFAULT_CITY = "Москва"
        private const val DEFAULT_LATITUDE = 55.7522
        private const val DEFAULT_LONGITUDE = 37.6156
    }
}
