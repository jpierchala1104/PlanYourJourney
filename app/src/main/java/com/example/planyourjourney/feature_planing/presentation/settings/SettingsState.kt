package com.example.planyourjourney.feature_planing.presentation.settings

import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.presentation.util.Language
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType

data class SettingsState (
    val isLoading: Boolean = false,
    val isSettingsLoaded: Boolean = false,
    val settings: Settings = Settings()
)