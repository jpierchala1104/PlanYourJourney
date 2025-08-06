package com.example.planyourjourney.feature_planing.domain.use_case

import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.util.Resource
import com.example.planyourjourney.feature_planing.domain.util.toKotlinLocalDate
import com.example.planyourjourney.feature_planing.domain.util.toLocalDate


import java.time.LocalDate

class ClearOldWeatherUseCase (
    private val repository: WeatherRepository
){
    suspend operator fun invoke(){
        var locationWeather = listOf<LocationWeather>()
        repository.getLocationsWithWeather().collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { weather ->
                        locationWeather = weather
                    }
                }
                is Resource.Error -> {}
                is Resource.Loading -> {}
            }
        }
        if (locationWeather.isEmpty()) return

        val yesterdayDate = LocalDate.now().minusDays(1).toKotlinLocalDate()

        val daysInDbToClear = locationWeather.first().hourlyWeatherList.filter { hourlyWeather ->
            hourlyWeather.time.hour == 0 && hourlyWeather.time.toLocalDate() < yesterdayDate
        }.map { it.time.toLocalDate() }

        daysInDbToClear.forEach { day ->
            repository.deleteHourlyWeathersWithDate(day.toString())
        }
    }
}
