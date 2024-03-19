package com.example.planyourjourney.feature_planing.data.data_source

import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.data.data_source.dto.OpenMeteoAPIEntity
import com.example.planyourjourney.feature_planing.data.mapper.toHourlyWeatherList
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface OpenMeteoAPI {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") parameters: String = "temperature_2m,relative_humidity_2m,precipitation_probability,rain,snowfall,cloud_cover,wind_speed_10m",
        @Query("timezone") timezone: String = "auto"
    ): OpenMeteoAPIEntity

    companion object {
        const val BASE_URL = "https://api.open-meteo.com/v1/"
    }
}

//class OpenMeteoAPIImpl(
//    private val openMeteoAPI: OpenMeteoAPI
//) : WeatherDataSource {
//    override suspend fun getWeather(latitude: Double, longitude: Double): List<HourlyWeather> {
//        return openMeteoAPI.getWeather(latitude, longitude).toHourlyWeatherList()
//    }
//}
