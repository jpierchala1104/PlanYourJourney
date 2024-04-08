package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class GetWeatherAtLocationUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(location: Location): Flow<Resource<LocationWeather>> {
        return repository.getWeatherAtLocation(location)
    }
}