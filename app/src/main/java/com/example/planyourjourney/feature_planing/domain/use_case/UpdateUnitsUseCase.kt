package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class UpdateUnitsUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(settings: Settings, oldUnits: WeatherUnits) {
        return repository.updateUnits(settings, oldUnits)
    }
}