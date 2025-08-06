package com.example.planyourjourney.feature_planing.domain.model

import com.example.planyourjourney.feature_planing.domain.util.Language
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val language: Language = Language.English,
    val weatherUnits: WeatherUnits = WeatherUnits(),
    val weatherVariables: WeatherVariables = WeatherVariables(),
    val widgetLocation: Location? = null
)
