package com.example.planyourjourney.feature_planing.domain.use_case

data class WeatherDetailsUseCases(
    val getWeatherAtLocationUseCase: GetWeatherAtLocationUseCase,
    val getSettingsUseCase: GetSettingsUseCase,
    val saveSettingsUseCase: SaveSettingsUseCase
)