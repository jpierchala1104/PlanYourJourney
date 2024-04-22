package com.example.planyourjourney.feature_planing.domain.model

data class LocationWeather(
    val location: Location,
    val hourlyWeatherList: List<HourlyWeather>
)
