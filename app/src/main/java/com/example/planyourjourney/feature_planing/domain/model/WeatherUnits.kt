package com.example.planyourjourney.feature_planing.domain.model

import com.example.planyourjourney.feature_planing.domain.util.PercentageUnits
import com.example.planyourjourney.feature_planing.domain.util.PrecipitationUnits
import com.example.planyourjourney.feature_planing.domain.util.TemperatureUnits
import com.example.planyourjourney.feature_planing.domain.util.WindSpeedUnits
import kotlinx.serialization.Serializable

@Serializable
data class WeatherUnits(
    val temperatureUnits: TemperatureUnits = TemperatureUnits.CELSIUS,
    val precipitationUnits: PrecipitationUnits = PrecipitationUnits.MILLIMETERS,
    val windSpeedUnits: WindSpeedUnits = WindSpeedUnits.KILOMETERS_PER_HOUR,
    val percentageUnits: PercentageUnits = PercentageUnits.PERCENTAGES
)
