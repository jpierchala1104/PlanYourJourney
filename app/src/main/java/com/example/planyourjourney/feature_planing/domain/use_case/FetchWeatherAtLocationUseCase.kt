package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.util.APIFetchResult
import kotlinx.coroutines.flow.Flow

class FetchWeatherAtLocationUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(location: Location, weatherUnits: WeatherUnits): Flow<APIFetchResult> {
        return repository.fetchWeatherFromAPI(location, weatherUnits)
    }
}