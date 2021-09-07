package com.example.weatherforecast

import android.view.View
import android.widget.ProgressBar
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.weather_api.WeatherInfo

class ViewHandler(private val bindingClass: ActivityMainBinding) : ViewHandlerInterface {

    override fun storage(locationInfo: LocationInfo, weatherInfo: WeatherInfo?) {
        showCity(locationInfo.cityName)
        if (weatherInfo != null) {
            val timeData: List<String> = DataConverter().convertCurrentTime(
                weatherInfo.current.dt,
                weatherInfo.current.sunrise,
                weatherInfo.current.sunset
            )
            val pressure = DataConverter().hPaConvertToMmHg(weatherInfo.current.pressure)

            bindingClass.infoLayout.visibility = View.VISIBLE

            showCurrentTemperature(
                weatherInfo.current.temp.toInt().toString(),
                weatherInfo.current.feels_like.toInt().toString()
            )
            showWeatherParameters(
                weatherInfo.current.weather[0].description,
                timeData[1],
                timeData[2],
                weatherInfo.current.wind_speed.toInt().toString(),
                weatherInfo.current.humidity.toInt().toString(),
                pressure
            )
            showLastUpdateTime(timeData[0])
        }
        bindingClass.weatherInfoLoading.visibility = ProgressBar.GONE
    }

    override fun showCity(city: String) {
        bindingClass.currentLocation.visibility = View.VISIBLE
        bindingClass.currentLocation.hint = city
    }

    override fun showCurrentTemperature(temp: String, feelsLike: String) {
        bindingClass.currentTemperature.visibility = View.VISIBLE
        bindingClass.currentTemperature.text = "$temp°C"

        bindingClass.feelsTemperature.visibility = View.VISIBLE
        bindingClass.feelsTemperature.text = "Ощущается как $feelsLike°C"
    }

    override fun showWeatherParameters(
        description: String,
        sunrise: String,
        sunset: String,
        windSpeed: String,
        humidity: String,
        pressure: String
    ) {
        bindingClass.weatherType.visibility = View.VISIBLE
        bindingClass.weatherType.text = description

        bindingClass.sunRiseLayout.visibility = View.VISIBLE
        bindingClass.sunRiseValue.text = sunrise

        bindingClass.sunSetLayout.visibility = View.VISIBLE
        bindingClass.sunSetValue.text = sunset

        bindingClass.windLayout.visibility = View.VISIBLE
        bindingClass.windValue.text = "$windSpeed м/с"

        bindingClass.humidityLayout.visibility = View.VISIBLE
        bindingClass.humidityValue.text = "$humidity %"

        bindingClass.pressureLayout.visibility = View.VISIBLE
        bindingClass.pressureValue.text = "$pressure мм рт. ст."
    }

    override fun showLastUpdateTime(dt: String) {
        bindingClass.lastUpdate.visibility = View.VISIBLE
        bindingClass.lastUpdate.text = "Последнее обновление: $dt"
    }

    override fun showLoading() {
        bindingClass.currentLocation.visibility = View.GONE
        bindingClass.lastUpdate.visibility = View.GONE
        bindingClass.weatherType.visibility = View.GONE
        bindingClass.currentTemperature.visibility = View.GONE
        bindingClass.feelsTemperature.visibility = View.GONE
        bindingClass.sunRiseLayout.visibility = View.GONE
        bindingClass.sunSetLayout.visibility = View.GONE
        bindingClass.windLayout.visibility = View.GONE
        bindingClass.humidityLayout.visibility = View.GONE
        bindingClass.pressureLayout.visibility = View.GONE
        bindingClass.infoLayout.visibility = View.GONE

        bindingClass.weatherInfoLoading.visibility = ProgressBar.VISIBLE
    }
}