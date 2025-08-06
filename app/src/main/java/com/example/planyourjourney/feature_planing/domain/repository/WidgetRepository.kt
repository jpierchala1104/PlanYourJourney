package com.example.planyourjourney.feature_planing.domain.repository

import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.Settings

interface WidgetRepository {
    suspend fun  getSettings(): Settings
    suspend fun  getPreloadedData(): LocationWeather
}