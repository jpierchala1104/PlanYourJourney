package com.example.planyourjourney.feature_planing.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationWeather(
    val location: Location,
    val hourlyWeatherList: List<HourlyWeather>
)
