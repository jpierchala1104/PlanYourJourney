package com.example.planyourjourney.feature_planing.domain.use_case

data class WeatherUseCases(
    val getLocationsWithWeatherUseCase : GetLocationsWithWeatherUseCase,
    val fetchWeatherAtLocationUseCase: FetchWeatherAtLocationUseCase,
    val insertLocationUseCase: InsertLocationUseCase,
    val deleteLocationUseCase: DeleteLocationUseCase,
    val deleteWeatherAtLocationUseCase: DeleteWeatherAtLocationUseCase,
    val getSettingsUseCase: GetSettingsUseCase,
    val clearOldWeatherUseCase: ClearOldWeatherUseCase,
    val preloadWidgetDataUseCase: PreloadWidgetDataUseCase
)
