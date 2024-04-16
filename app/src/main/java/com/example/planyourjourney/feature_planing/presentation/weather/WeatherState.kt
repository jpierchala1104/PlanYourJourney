package com.example.planyourjourney.feature_planing.presentation.weather

import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.presentation.util.Language
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType

data class WeatherState (
    val locationWeatherList: List<LocationWeather> = listOf(),
    val isLoading: Boolean = false,
    val isWeatherLoaded: Boolean = false,
    val settings: Settings = Settings()
)