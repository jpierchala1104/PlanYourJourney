package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class GetLocationsUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Location>>> {
        return repository.getLocations()
    }
}