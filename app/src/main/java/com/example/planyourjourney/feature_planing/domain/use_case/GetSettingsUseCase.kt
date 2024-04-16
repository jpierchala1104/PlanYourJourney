package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): Flow<Settings> {
        return repository.getSettings()
    }
}