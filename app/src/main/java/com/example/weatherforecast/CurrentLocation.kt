package com.example.weatherforecast

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class CurrentLocation(private var context: Context) {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getLastLocation(): LocationInfo? {
        return when (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            PackageManager.PERMISSION_GRANTED -> writeDownLocation(fusedLocationClient.lastLocation.addOnSuccessListener { })
            else -> null
        }
    }

    private fun writeDownLocation(taskLocation: Task<Location>): LocationInfo {
        val location = taskLocation.result
        return LocationInfo(
            cityName = Geocoder(context).getFromLocation(
                location.latitude,
                location.longitude,
                1
            ).first().subLocality,
            latitude = location.latitude,
            longitude = location.longitude
        )
    }
}
