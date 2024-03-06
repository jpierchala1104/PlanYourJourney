package com.example.planyourjourney.services

import com.example.planyourjourney.services.dataClasses.WeatherJson
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastService {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double ,
        @Query("longitude") longitude: Double,
        @Query("hourly") parameters: String = "temperature_2m,relative_humidity_2m,precipitation_probability,rain,snowfall,cloud_cover,wind_speed_10m",
        @Query("timezone") timezone: String = "auto"
    ): WeatherJson
}