package com.example.planyourjourney.feature_planing.data.repository

import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.repository.SettingsOperations
import com.example.planyourjourney.feature_planing.domain.repository.WidgetPreloadOperations
import com.example.planyourjourney.feature_planing.domain.repository.WidgetRepository
import javax.inject.Inject

class WidgetRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsOperations,
    private val widgetPreloadDataStore: WidgetPreloadOperations
) : WidgetRepository {
    override suspend fun getSettings(): Settings {
        return settingsDataStore.readSettingsInWidget()
    }

    override suspend fun getPreloadedData(): LocationWeather {
        return widgetPreloadDataStore.readData()
    }
}