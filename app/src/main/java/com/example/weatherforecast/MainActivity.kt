package com.example.weatherforecast

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.input.InputManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputBinding
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.weather_api.WeatherInfo
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityMainBinding
    private lateinit var viewHandlerInterface: ViewHandlerInterface

    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        viewHandlerInterface = ViewHandler(bindingClass)
    }

    override fun onStart() {
        super.onStart()

        checkPermission()
        CoroutineScope(Dispatchers.Main).launch {
            getCustomLocation()
        }
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
            CoroutineScope(Dispatchers.Main).launch {
                getCurrentLocation(context, false)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode == REQUEST_PERMISSION_CODE) {
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                CoroutineScope(Dispatchers.Main).launch {
                    getCurrentLocation(context, false)
                }
            }
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED -> {
                CoroutineScope(Dispatchers.Main).launch {
                    getCurrentLocation(context, true)
                }
            }
        }
    }

    private suspend fun getCurrentLocation(context: Context, defaultConfiguration: Boolean) =
        coroutineScope {
            val location: LocationInfo = withContext(Dispatchers.IO) {
                if (defaultConfiguration) {
                    LocationManager(context).setCurrentLocation(true)
                } else {
                    LocationManager(context).setCurrentLocation(false)
                }
            }
            getWeatherInfo(location)
        }

    private suspend fun getCustomLocation() = coroutineScope {
        bindingClass.currentLocation.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    run {
                        viewHandlerInterface.showLoading()
                        CoroutineScope(Dispatchers.Main).launch {
                            val newLocation: LocationInfo = withContext(Dispatchers.IO) {
                                LocationManager(context).setCustomLocation(bindingClass.currentLocation.text.toString())
                            }
                            getWeatherInfo(newLocation)
                        }
                    }
                    if (bindingClass.currentLocation.isFocused) {
                        bindingClass.currentLocation.clearFocus()
                        bindingClass.currentLocation.hint = ""
                    }
                    return true
                }
                return false
            }
        })
    }

    private suspend fun getWeatherInfo(locationInfo: LocationInfo) = coroutineScope {
        val weatherInfo: WeatherInfo = withContext(Dispatchers.IO) {
            WeatherManager(locationInfo).getCurrentLocationData()
        }
        viewHandlerInterface.storage(locationInfo, weatherInfo)
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 123
    }
}