package com.example.planyourjourney.feature_planing.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class LocationWithHourlyWeather(
    @Embedded val location: LocationEntity,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "locationWeatherId"
    )
    val hourlyWeather: List<HourlyWeatherEntity>
)
