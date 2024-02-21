package com.example.planyourjourney.services.dataClasses

import java.time.LocalDateTime

data class Weather(
    val time: LocalDateTime,
    val temperature: Double,
    val relativeHumidity: Int,
    val precipitationProbability: Int,
    val precipitation: Double,
    val rain: Double,
    val windSpeed: Double
)