package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository

class SaveSettingsUseCase (
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(settings: Settings) {
        repository.saveSettings(settings)
    }
}