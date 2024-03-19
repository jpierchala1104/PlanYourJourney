package com.example.planyourjourney.feature_planing.domain.model

import java.time.LocalDateTime


data class HourlyWeather(
    val time: LocalDateTime,
    val temperature2m: Double,
    val relativeHumidity2m: Int,
    val precipitationProbability: Int,
    val rain: Double,
    val snowfall: Double,
    val cloudCover: Int,
    val windSpeed10m: Double
)
