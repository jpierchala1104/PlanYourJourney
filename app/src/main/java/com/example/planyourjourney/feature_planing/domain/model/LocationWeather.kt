package com.example.planyourjourney.feature_planing.domain.model

data class LocationWeather(
    var location: Location,
    val hourlyWeatherList: List<HourlyWeather>
)
