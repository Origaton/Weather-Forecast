package com.example.weatherforecast

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.os.TokenWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.weather_api.WeatherInfo
import com.google.gson.Gson
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityMainBinding
    private lateinit var viewHandlerInterface: ViewHandlerInterface
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var locationInfo: LocationInfo
    private lateinit var weatherInfo: WeatherInfo

    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        viewHandlerInterface = ViewHandler(bindingClass)
        sharedPreference = getPreferences(Context.MODE_PRIVATE)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        CoroutineScope(Dispatchers.Main).launch {
            checkPermission()
        }
    }

    override fun onStart() {
        super.onStart()

        if (sharedPreference.contains("location") && sharedPreference.contains("weather")) {
            bindingClass.weatherInfoUpdating.visibility = ProgressBar.VISIBLE
            Log.d("location_test", "fuck")
            val savedLocation: LocationInfo =
                Gson().fromJson(
                    sharedPreference.getString("location", ""),
                    LocationInfo::class.java
                )
            val savedWeather: WeatherInfo =
                Gson().fromJson(sharedPreference.getString("weather", ""), WeatherInfo::class.java)
            Log.d("location_test", "$savedLocation")
            if (savedLocation.cityName != null) {
                viewHandlerInterface.storage(savedLocation, savedWeather)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            getCustomLocation()
            refresh()
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
                    Toast.makeText(
                        context,
                        "Показано местоположение по умолчанию",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private suspend fun getCurrentLocation(context: Context, defaultConfiguration: Boolean) =
        coroutineScope {
            locationInfo = withContext(Dispatchers.IO) {
                if (defaultConfiguration) {
                    LocationManager(context).setCurrentLocation(true)
                } else {
                    LocationManager(context).setCurrentLocation(false)
                }
            }
            getWeatherInfo(locationInfo)
        }

    private suspend fun getCustomLocation() = coroutineScope {
        val field = bindingClass.currentLocation

        field.setOnFocusChangeListener { _, _ ->
            field.hint = ""
        }

        field.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    run {
                        viewHandlerInterface.showLoading()
                        CoroutineScope(Dispatchers.Main).launch {
                            locationInfo = withContext(Dispatchers.IO) {
                                LocationManager(context).setCustomLocation(field.text.toString())
                            }
                            if (locationInfo.latitude != null && locationInfo.longitude != null) {
                                getWeatherInfo(locationInfo)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Некорректное название города",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewHandlerInterface.hideLoading()
                            }
                        }

                        val i = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        i.hideSoftInputFromWindow(field.windowToken, 0)

                        field.clearFocus()
                        field.hint = ""
                    }

                    return true
                }
                return false
            }
        })
    }

    private suspend fun getWeatherInfo(locationInfo: LocationInfo) = coroutineScope {
        weatherInfo = withContext(Dispatchers.IO) {
            WeatherManager(locationInfo).getCurrentLocationData()
        }
        viewHandlerInterface.storage(locationInfo, weatherInfo)
        bindingClass.weatherInfoUpdating.visibility = ProgressBar.GONE
    }

    private fun refresh() {
        bindingClass.swipe.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (::locationInfo.isInitialized) {
                    getWeatherInfo(locationInfo)
                    bindingClass.swipe.isRefreshing = false
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (::locationInfo.isInitialized && ::weatherInfo.isInitialized) {
            val editor = sharedPreference.edit()
            editor.putString("location", Gson().toJson(locationInfo))
            editor.putString("weather", Gson().toJson(weatherInfo))
            editor.apply()
        }
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 123
    }
}