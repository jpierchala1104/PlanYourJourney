package com.example.planyourjourney.feature_planing.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity

data class HourlyWeatherEntity(
    @PrimaryKey val hourlyWeatherId: Int? = null,
    val locationWeatherId: Int? = null,
    val time: String,
    val temperature2m: Double,
    val relativeHumidity2m: Int,
    val precipitationProbability: Int,
    @ColumnInfo(defaultValue = "0.0")val precipitation: Double,
    val rain: Double,
    val snowfall: Double,
    val cloudCover: Int,
    val windSpeed10m: Double
)
