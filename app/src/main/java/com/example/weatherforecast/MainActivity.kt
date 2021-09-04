package com.example.weatherforecast

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.weatherforecast.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityMainBinding
    private lateinit var viewHandlerInterface: ViewHandlerInterface
    private lateinit var locationInfo: LocationInfo

    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        viewHandlerInterface = ViewHandler(bindingClass)
        checkPermission()
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_CODE
            )
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                getCurrentLocation(context)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CoroutineScope(Dispatchers.IO).launch {
                    getCurrentLocation(context)
                }
            } else {
                locationInfo = LocationInfo(DEFAULT_CITY, DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
            }
        }
    }

    private suspend fun getCurrentLocation(context: Context) = coroutineScope {
        val info = async(Dispatchers.IO) { CurrentLocation(context).getLastLocation() }
        locationInfo = if (info.await() != null) {
            info.await()!!
        } else {
            LocationInfo(DEFAULT_CITY, DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        }
        viewHandlerInterface.realiseButton(locationInfo)
    }


    companion object {
        private const val REQUEST_PERMISSION_CODE = 123
        private const val DEFAULT_CITY = "Москва"
        private const val DEFAULT_LATITUDE = 55.7522
        private const val DEFAULT_LONGITUDE = 37.6156
    }
}