package com.example.planyourjourney.feature_planing.domain.use_case

data class PlaningUseCases(
    val getLocationsUseCase: GetLocationsUseCase,
    val fetchWeatherAtLocationUseCase: FetchWeatherAtLocationUseCase,
    val insertLocationUseCase: InsertLocationUseCase,
    val deleteLocationUseCase: DeleteLocationUseCase,
    val deleteWeatherAtLocationUseCase: DeleteWeatherAtLocationUseCase,
    val getSettingsUseCase: GetSettingsUseCase
)
