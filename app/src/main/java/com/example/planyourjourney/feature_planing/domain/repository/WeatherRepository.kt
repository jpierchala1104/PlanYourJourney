package com.example.planyourjourney.feature_planing.domain.repository

import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.util.APIFetchResult
import com.example.planyourjourney.feature_planing.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherAtLocation(location: Location): Flow<Resource<LocationWeather>>
    suspend fun fetchWeatherFromAPI(location: Location, weatherUnits: WeatherUnits): Flow<APIFetchResult>
    suspend fun getLocationsWithWeather(): Flow<Resource<List<LocationWeather>>>
    suspend fun getLocations(): Flow<Resource<List<Location>>>
    suspend fun getLocationId(location: Location): Int?
    suspend fun insertLocation(location: Location)
    suspend fun deleteLocation(location: Location)
    suspend fun deleteLocations()
    suspend fun insertHourlyWeathers(hourlyWeather: List<HourlyWeather>)
    suspend fun deleteHourlyWeathers()
    suspend fun deleteHourlyWeathersAtLocation(locationId: Int)
    suspend fun saveSettings(settings: Settings)
    suspend fun getSettings(): Flow<Settings>
    suspend fun deleteHourlyWeathersWithDate(date: String)
    suspend fun updateUnits(settings: Settings, oldUnits: WeatherUnits)
}
