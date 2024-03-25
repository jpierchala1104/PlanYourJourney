package com.example.planyourjourney.feature_planing.domain.repository

import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(location: Coordinates, weatherUnits: WeatherUnits): Flow<Resource<List<HourlyWeather>>>
}