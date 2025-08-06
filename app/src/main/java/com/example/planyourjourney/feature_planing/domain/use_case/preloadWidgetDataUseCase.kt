package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository

class PreloadWidgetDataUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(locationWeather: LocationWeather){
        repository.preloadWidgetData(locationWeather)
    }
}