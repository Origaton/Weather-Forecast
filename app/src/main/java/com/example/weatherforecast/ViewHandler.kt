package com.example.weatherforecast

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.weather_api.WeatherInfo

class ViewHandler(private val bindingClass: ActivityMainBinding) : ViewHandlerInterface {

    override fun storage(locationInfo: LocationInfo, weatherInfo: WeatherInfo?) {
        showCurrentWeather(locationInfo, weatherInfo)
        showForecast(weatherInfo)
    }

    override fun showCurrentWeather(locationInfo: LocationInfo, weatherInfo: WeatherInfo?) {
        showCity(locationInfo.cityName!!)
        if (weatherInfo != null) {
            val timeData: List<String> = DataConverter().convertCurrentTime(
                weatherInfo.current.dt,
                weatherInfo.current.sunrise,
                weatherInfo.current.sunset
            )
            val currentTime = timeData[0]
            val sunRiseTime = timeData[1]
            val sunSetTime = timeData[2]
            val pressure = DataConverter().hPaConvertToMmHg(weatherInfo.current.pressure)

            showCurrentTemperature(
                weatherInfo.current.temp.toInt().toString(),
                weatherInfo.current.feels_like.toInt().toString()
            )
            showWeatherParameters(
                weatherInfo.current.weather[0].description,
                sunRiseTime,
                sunSetTime,
                weatherInfo.current.wind_speed.toInt().toString(),
                weatherInfo.current.humidity.toInt().toString(),
                pressure,
                weatherInfo.current.clouds.toInt().toString()
            )
            showLastUpdateTime(currentTime)
        }
        bindingClass.easterEgg.visibility = View.VISIBLE
        bindingClass.forecastLayout.visibility = View.VISIBLE
        bindingClass.weatherInfoLoading.visibility = ProgressBar.GONE
    }

    override fun showCity(city: String) {
        bindingClass.searchImage.visibility = View.VISIBLE

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
        pressure: String,
        cloudiness: String
    ) {
        bindingClass.weatherType.visibility = View.VISIBLE
        bindingClass.weatherType.text = "На улице $description"

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

        bindingClass.cloudinessLayout.visibility = View.VISIBLE
        bindingClass.cloudinessValue.text = "$cloudiness %"
    }

    override fun showLastUpdateTime(dt: String) {
        bindingClass.lastUpdate.visibility = View.VISIBLE
        bindingClass.lastUpdate.text = "Последнее обновление: $dt"
    }

    override fun showForecast(weatherInfo: WeatherInfo?) {
        if (weatherInfo != null) {
            showHourlyForecast(weatherInfo)
            showDailyForecast(weatherInfo)
        }
    }

    override fun showHourlyForecast(weatherInfo: WeatherInfo) {
        //one hour
        setHourlyWeather(
            bindingClass.afterOneHour,
            bindingClass.afterOneHourImage,
            bindingClass.afterOneHourTemperature,
            weatherInfo,
            1
        )

        //two hours
        setHourlyWeather(
            bindingClass.afterTwoHours,
            bindingClass.afterTwoHoursImage,
            bindingClass.afterTwoHoursTemperature,
            weatherInfo,
            2
        )

        //three hours
        setHourlyWeather(
            bindingClass.afterThreeHours,
            bindingClass.afterThreeHoursImage,
            bindingClass.afterThreeHoursTemperature,
            weatherInfo,
            3
        )

        //four hours
        setHourlyWeather(
            bindingClass.afterFourHours,
            bindingClass.afterFourHoursImage,
            bindingClass.afterFourHoursTemperature,
            weatherInfo,
            4
        )

        //five hours
        setHourlyWeather(
            bindingClass.afterFiveHours,
            bindingClass.afterFiveHoursImage,
            bindingClass.afterFiveHoursTemperature,
            weatherInfo,
            5
        )
    }

    override fun setHourlyWeather(
        textView: TextView,
        imageView: ImageView,
        textViewTemp: TextView,
        weatherInfo: WeatherInfo,
        hour: Int
    ) {
        textView.text =
            DataConverter().convertHourlyTime(weatherInfo.hourly[hour].dt)
        showWeatherImage(
            weatherInfo.hourly[hour].weather[0].id.toString()[0],
            imageView
        )
        if (weatherInfo.hourly[hour].weather[0].id == 800) {
            imageView.setImageResource(R.drawable.sunny)
        }
        textViewTemp.text = "${weatherInfo.hourly[hour].temp.toInt()}°C"
    }

    override fun showDailyForecast(weatherInfo: WeatherInfo) {
        //one day
        setDailyWeather(
            bindingClass.afterOneDay,
            bindingClass.afterOneDayImage,
            bindingClass.afterOneDayTemperature,
            weatherInfo,
            1
        )

        //two days
        setDailyWeather(
            bindingClass.afterTwoDays,
            bindingClass.afterTwoDaysImage,
            bindingClass.afterTwoDaysTemperature,
            weatherInfo,
            2
        )

        //three days
        setDailyWeather(
            bindingClass.afterThreeDays,
            bindingClass.afterThreeDaysImage,
            bindingClass.afterThreeDaysTemperature,
            weatherInfo,
            3
        )

        //four days
        setDailyWeather(
            bindingClass.afterFourDays,
            bindingClass.afterFourDaysImage,
            bindingClass.afterFourDaysTemperature,
            weatherInfo,
            4
        )

        //five days
        setDailyWeather(
            bindingClass.afterFiveDays,
            bindingClass.afterFiveDaysImage,
            bindingClass.afterFiveDaysTemperature,
            weatherInfo,
            5
        )

        //six days
        setDailyWeather(
            bindingClass.afterSixDays,
            bindingClass.afterSixDaysImage,
            bindingClass.afterSixDaysTemperature,
            weatherInfo,
            6
        )

        //seven days
        setDailyWeather(
            bindingClass.afterSevenDays,
            bindingClass.afterSevenDaysImage,
            bindingClass.afterSevenDaysTemperature,
            weatherInfo,
            7
        )
    }

    override fun setDailyWeather(
        textView: TextView,
        imageView: ImageView,
        textViewTemp: TextView,
        weatherInfo: WeatherInfo,
        day: Int
    ) {
        textView.text =
            DataConverter().convertDailyTime(weatherInfo.daily[day].dt)
        showWeatherImage(
            weatherInfo.daily[day].weather[0].id.toString()[0],
            imageView
        )
        if (weatherInfo.daily[day].weather[0].id == 800) {
            imageView.setImageResource(R.drawable.sunny)
        }
        textViewTemp.text =
            "${weatherInfo.daily[day].temp.max.toInt()}°C / ${weatherInfo.daily[day].temp.min.toInt()}°C"
    }

    override fun showWeatherImage(id: Char, imageView: ImageView) {
        when (id) {
            '2' -> imageView.setImageResource(R.drawable.thunderstorm)
            '3' -> imageView.setImageResource(R.drawable.very_cloudy)
            '5' -> imageView.setImageResource(R.drawable.rainy)
            '6' -> imageView.setImageResource(R.drawable.snowy)
            '7' -> imageView.setImageResource(R.drawable.fog)
            '8' -> imageView.setImageResource(R.drawable.cloudy)
        }
    }

    override fun showLoading() {
        bindingClass.currentLocation.visibility = View.GONE
        bindingClass.searchImage.visibility = View.GONE
        bindingClass.lastUpdate.visibility = View.GONE
        bindingClass.weatherType.visibility = View.GONE
        bindingClass.currentTemperature.visibility = View.GONE
        bindingClass.feelsTemperature.visibility = View.GONE
        bindingClass.sunRiseLayout.visibility = View.GONE
        bindingClass.sunSetLayout.visibility = View.GONE
        bindingClass.windLayout.visibility = View.GONE
        bindingClass.humidityLayout.visibility = View.GONE
        bindingClass.pressureLayout.visibility = View.GONE
        bindingClass.cloudinessLayout.visibility = View.GONE
        bindingClass.forecastLayout.visibility = View.GONE
        bindingClass.easterEgg.visibility = View.GONE

        bindingClass.weatherInfoLoading.visibility = ProgressBar.VISIBLE
    }

    override fun hideLoading() {
        bindingClass.currentLocation.visibility = View.VISIBLE
        bindingClass.searchImage.visibility = View.VISIBLE
        bindingClass.lastUpdate.visibility = View.VISIBLE
        bindingClass.weatherType.visibility = View.VISIBLE
        bindingClass.currentTemperature.visibility = View.VISIBLE
        bindingClass.feelsTemperature.visibility = View.VISIBLE
        bindingClass.sunRiseLayout.visibility = View.VISIBLE
        bindingClass.sunSetLayout.visibility = View.VISIBLE
        bindingClass.windLayout.visibility = View.VISIBLE
        bindingClass.humidityLayout.visibility = View.VISIBLE
        bindingClass.pressureLayout.visibility = View.VISIBLE
        bindingClass.cloudinessLayout.visibility = View.VISIBLE
        bindingClass.forecastLayout.visibility = View.VISIBLE
        bindingClass.easterEgg.visibility = View.VISIBLE

        bindingClass.weatherInfoLoading.visibility = ProgressBar.GONE
    }
}