package com.example.planyourjourney.feature_planing.domain.model

import com.example.planyourjourney.feature_planing.presentation.util.Language
import com.example.planyourjourney.feature_planing.presentation.util.OutputType
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType

data class Settings(
    val language: Language,
    val searchInputType: SearchInputType,
    val outputType: OutputType,
    val weatherUnits: WeatherUnits
)
