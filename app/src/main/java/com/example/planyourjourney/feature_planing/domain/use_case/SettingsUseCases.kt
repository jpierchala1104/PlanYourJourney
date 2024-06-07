package com.example.planyourjourney.feature_planing.domain.use_case

data class SettingsUseCases (
    val saveSettingsUseCase: SaveSettingsUseCase,
    val getSettingsUseCase: GetSettingsUseCase,
    val updateUnitsUseCase: UpdateUnitsUseCase
)