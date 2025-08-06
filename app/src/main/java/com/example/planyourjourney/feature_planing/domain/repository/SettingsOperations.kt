package com.example.planyourjourney.feature_planing.domain.repository

import com.example.planyourjourney.feature_planing.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsOperations {
    suspend fun saveSettings(settings: Settings)
    fun readSettingsState(): Flow<Settings>
    suspend fun readSettingsInWidget(): Settings
}