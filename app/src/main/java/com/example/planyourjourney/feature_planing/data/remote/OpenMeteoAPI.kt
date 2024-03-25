package com.example.planyourjourney.feature_planing.data.remote

import com.example.planyourjourney.feature_planing.data.remote.dto.OpenMeteoAPIEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoAPI {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") parameters: String = "temperature_2m,relative_humidity_2m,precipitation_probability,rain,snowfall,cloud_cover,wind_speed_10m",
        @Query("temperature_unit") temperatureUnit: String = "celsius",
        @Query("precipitation_unit") precipitationUnit: String = "mm",
        @Query("wind_speed_unit") windSpeedUnit: String = "kmh",
        @Query("timezone") timezone: String = "auto"
    ): OpenMeteoAPIEntity

    companion object {
        const val BASE_URL = "https://api.open-meteo.com/v1/"
    }
}