package com.example.planyourjourney.feature_planing.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherVariables(
    val isTemperature2mChecked: Boolean = true,
    val isPrecipitationProbabilityChecked: Boolean = true,
    val isPrecipitationChecked: Boolean = true,
    val isRainChecked: Boolean = false,
    val isSnowfallChecked: Boolean = false,
    val isCloudCoverChecked: Boolean = false,
    val isRelativeHumidity2mChecked: Boolean = false,
    val isWindSpeed10mChecked: Boolean = false
)
