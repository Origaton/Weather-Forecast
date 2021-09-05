package com.example.weatherforecast

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationManager(private var context: Context, private val bindingClass: ActivityMainBinding) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewHandlerInterface: ViewHandlerInterface

    fun setLocation(defaultConfiguration: Boolean, locationInfo: LocationInfo?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        viewHandlerInterface = ViewHandler(bindingClass)
        if (defaultConfiguration && locationInfo != null) {
            setDefaultLocation(locationInfo)
        } else {
            setCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun setCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            viewHandlerInterface.realiseButton(
                LocationInfo(
                    cityName = Geocoder(context).getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    ).first().subLocality,
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
        }
    }

    private fun setDefaultLocation(locationInfo: LocationInfo) {
        viewHandlerInterface.realiseButton(locationInfo)
    }
}
