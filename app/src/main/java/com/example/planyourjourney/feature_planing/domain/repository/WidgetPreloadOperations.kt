package com.example.planyourjourney.feature_planing.domain.repository

import com.example.planyourjourney.feature_planing.domain.model.LocationWeather


interface WidgetPreloadOperations {
    suspend fun saveData(locationWeather: LocationWeather)
    suspend fun readData(): LocationWeather
}