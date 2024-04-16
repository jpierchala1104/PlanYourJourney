package com.example.planyourjourney.feature_planing.domain.model

import com.example.planyourjourney.feature_planing.presentation.util.Language
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val language: Language = Language.English,
    //val searchInputType: SearchInputType = SearchInputType.LocationName,
    val weatherUnits: WeatherUnits = WeatherUnits()
)
