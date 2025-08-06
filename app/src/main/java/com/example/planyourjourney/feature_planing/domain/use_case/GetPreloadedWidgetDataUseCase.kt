package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.repository.WidgetRepository

class GetPreloadedWidgetDataUseCase(
    private val repository:WidgetRepository
){
    suspend operator fun invoke(): LocationWeather {
        return repository.getPreloadedData()
    }
}