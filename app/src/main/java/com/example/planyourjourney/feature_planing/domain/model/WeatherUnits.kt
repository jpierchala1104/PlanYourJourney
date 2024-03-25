package com.example.planyourjourney.feature_planing.domain.model

import com.example.planyourjourney.feature_planing.presentation.util.PercentageUnits
import com.example.planyourjourney.feature_planing.presentation.util.PrecipitationUnits
import com.example.planyourjourney.feature_planing.presentation.util.TemperatureUnits
import com.example.planyourjourney.feature_planing.presentation.util.WindSpeedUnits

data class WeatherUnits(
    val temperatureUnits: TemperatureUnits = TemperatureUnits.CELSIUS,
    val precipitationUnits: PrecipitationUnits = PrecipitationUnits.MILLIMETERS,
    val windSpeedUnits: WindSpeedUnits = WindSpeedUnits.KILOMETERS_PER_HOUR,
    val percentageUnits: PercentageUnits = PercentageUnits.PERCENTAGES
)
