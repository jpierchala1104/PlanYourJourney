package com.example.planyourjourney.services

import com.example.planyourjourney.services.dataClasses.Coordinates
import com.example.planyourjourney.services.dataClasses.WeatherJson

class WeatherRepository {
    private val weatherForecastService = RetrofitInstance.weatherForecastService

    suspend fun getWeather(location: Coordinates): WeatherJson {
        return weatherForecastService.getWeather(location.latitude, location.longitude)
    }
}