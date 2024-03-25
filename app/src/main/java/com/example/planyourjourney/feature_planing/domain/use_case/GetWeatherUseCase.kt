package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(location: Coordinates, weatherUnits: WeatherUnits): Flow<Resource<List<HourlyWeather>>> {
        return repository.getWeather(location, weatherUnits)
    }
}