package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.repository.WidgetRepository

class GetSettingsForWidgetUseCase(
    private val repository: WidgetRepository
) {
    suspend operator fun invoke(): Settings {
        return repository.getSettings()
    }
}