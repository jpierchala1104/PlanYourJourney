package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    val repository: WeatherRepository
) {
    suspend operator fun invoke(location: Coordinates): Flow<Resource<List<HourlyWeather>>> {
        return repository.getWeather(location)
    }
}