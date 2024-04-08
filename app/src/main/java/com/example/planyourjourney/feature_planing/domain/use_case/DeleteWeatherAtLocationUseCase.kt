package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository

class DeleteWeatherAtLocationUseCase (
    private val repository: WeatherRepository
){
    suspend operator fun invoke(location: Location){
        repository.deleteHourlyWeathersAtLocation(location.locationId!!)
    }
}