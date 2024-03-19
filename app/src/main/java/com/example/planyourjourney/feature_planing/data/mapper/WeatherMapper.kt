package com.example.planyourjourney.feature_planing.data.mapper

import android.util.Log
import com.example.planyourjourney.feature_planing.data.data_source.dto.OpenMeteoAPIEntity
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import java.time.LocalDateTime

fun OpenMeteoAPIEntity.toHourlyWeatherList(): List<HourlyWeather> {
    var hourlyWeatherList: List<HourlyWeather> = listOf()
    for (i in 0 until hourly!!.time.size) {
        hourlyWeatherList = hourlyWeatherList + HourlyWeather(
            time = LocalDateTime.parse(hourly!!.time[i]),
            temperature2m = hourly!!.temperature2m[i],
            relativeHumidity2m = hourly!!.relativeHumidity2m[i],
            precipitationProbability = hourly!!.precipitationProbability[i],
            rain = hourly!!.rain[i],
            snowfall = hourly!!.snowfall[i],
            cloudCover = hourly!!.cloudCover[i],
            windSpeed10m = hourly!!.windSpeed10m[i]
        )
    }
    return hourlyWeatherList
}