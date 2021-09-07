package com.example.weatherforecast

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
    }

    override fun showCity(city: String) {

    }

    override fun showCurrentTemperature(temp: String, feelsLike: String) {
        bindingClass.currentTemperature.text = "$temp°C"
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
        bindingClass.weatherType.text = description
        bindingClass.sunRiseValue.text = sunrise
        bindingClass.sunSetValue.text = sunset
        bindingClass.windValue.text = "$windSpeed м/с"
        bindingClass.humidityValue.text = "$humidity %"
        bindingClass.pressureValue.text = "$pressure мм рт. ст."
    }

    override fun showLastUpdateTime(dt: String) {
        bindingClass.lastUpdate.text = "Последнее обновление: $dt"
    }

}