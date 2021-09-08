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
                pressure,
                weatherInfo.current.clouds.toInt().toString()
            )
            showLastUpdateTime(timeData[0])
        }
        bindingClass.weatherInfoLoading.visibility = ProgressBar.GONE
        bindingClass.easterEgg.visibility = View.VISIBLE
        bindingClass.forecastLayout.visibility = View.VISIBLE
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
        bindingClass.afterOneHour.text = DataConverter().convertHourlyTime(weatherInfo.hourly[1].dt)
        showWeatherImage(
            weatherInfo.hourly[1].weather[0].id.toString()[0],
            bindingClass.afterOneHourImage
        )
        if (weatherInfo.hourly[1].weather[0].id == 800) {
            bindingClass.afterOneHourImage.setImageResource(R.drawable.sunny)
        }
        bindingClass.afterOneHourTemperature.text = "${weatherInfo.hourly[1].temp.toInt()}°C"

        //two hours
        bindingClass.afterTwoHours.text =
            DataConverter().convertHourlyTime(weatherInfo.hourly[2].dt)
        showWeatherImage(
            weatherInfo.hourly[2].weather[0].id.toString()[0],
            bindingClass.afterTwoHoursImage
        )
        if (weatherInfo.hourly[2].weather[0].id == 800) {
            bindingClass.afterTwoHoursImage.setImageResource(R.drawable.sunny)
        }
        bindingClass.afterTwoHoursTemperature.text = "${weatherInfo.hourly[2].temp.toInt()}°C"

        //three hours
        bindingClass.afterThreeHours.text =
            DataConverter().convertHourlyTime(weatherInfo.hourly[3].dt)
        showWeatherImage(
            weatherInfo.hourly[3].weather[0].id.toString()[0],
            bindingClass.afterThreeHoursImage
        )
        if (weatherInfo.hourly[3].weather[0].id == 800) {
            bindingClass.afterThreeHoursImage.setImageResource(R.drawable.sunny)
        }
        bindingClass.afterThreeHoursTemperature.text = "${weatherInfo.hourly[3].temp.toInt()}°C"

        //four hours
        bindingClass.afterFourHours.text =
            DataConverter().convertHourlyTime(weatherInfo.hourly[4].dt)
        showWeatherImage(
            weatherInfo.hourly[4].weather[0].id.toString()[0],
            bindingClass.afterFourHoursImage
        )
        if (weatherInfo.hourly[4].weather[0].id == 800) {
            bindingClass.afterFourHoursImage.setImageResource(R.drawable.sunny)
        }
        bindingClass.afterFourHoursTemperature.text = "${weatherInfo.hourly[4].temp.toInt()}°C"

        //five hours
        bindingClass.afterFiveHours.text =
            DataConverter().convertHourlyTime(weatherInfo.hourly[5].dt)
        showWeatherImage(
            weatherInfo.hourly[5].weather[0].id.toString()[0],
            bindingClass.afterFiveHoursImage
        )
        if (weatherInfo.hourly[5].weather[0].id == 800) {
            bindingClass.afterFiveHoursImage.setImageResource(R.drawable.sunny)
        }
        bindingClass.afterFiveHoursTemperature.text = "${weatherInfo.hourly[5].temp.toInt()}°C"
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
            '7' -> imageView.setImageResource(R.drawable.hurricane)
            '8' -> imageView.setImageResource(R.drawable.cloudy)
        }
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
        bindingClass.cloudinessLayout.visibility = View.GONE
        bindingClass.forecastLayout.visibility = View.GONE
        bindingClass.easterEgg.visibility = View.GONE

        bindingClass.weatherInfoLoading.visibility = ProgressBar.VISIBLE
    }
}