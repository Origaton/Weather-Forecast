package com.example.weatherforecast

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherforecast.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityMainBinding

    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

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
            return
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
                CoroutineScope(Dispatchers.Default).launch {
                    getCurrentLocation(context)
                }
            } else {
                LocationManager(context, bindingClass).setLocation(
                    true,
                    LocationInfo(
                        DEFAULT_CITY,
                        DEFAULT_LATITUDE,
                        DEFAULT_LONGITUDE
                    )
                )

            }
        }
    }

    private suspend fun getCurrentLocation(context: Context) = coroutineScope {
        launch(Dispatchers.IO) {
            LocationManager(context, bindingClass).setLocation(false, null)
        }
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 123
        private const val DEFAULT_CITY = "Москва"
        private const val DEFAULT_LATITUDE = 55.7522
        private const val DEFAULT_LONGITUDE = 37.6156
    }
}