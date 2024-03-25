package com.example.planyourjourney.feature_planing.domain.model

import com.example.planyourjourney.feature_planing.domain.util.Language
import com.example.planyourjourney.feature_planing.domain.util.OutputType
import com.example.planyourjourney.feature_planing.domain.util.SearchInputType
import com.example.planyourjourney.feature_planing.presentation.util.WeatherUnits

data class Settings(
    val language: Language,
    val searchInputType: SearchInputType,
    val outputType: OutputType,
    val weatherUnits: WeatherUnits
)
