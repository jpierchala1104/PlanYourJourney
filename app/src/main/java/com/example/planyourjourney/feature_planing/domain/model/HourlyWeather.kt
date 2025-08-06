package com.example.planyourjourney.feature_planing.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class HourlyWeather(
    val time: LocalDateTime,
    val temperature2m: Double,
    val relativeHumidity2m: Int,
    val precipitationProbability: Int,
    val precipitation: Double,
    val rain: Double,
    val snowfall: Double,
    val cloudCover: Int,
    val windSpeed10m: Double
)
