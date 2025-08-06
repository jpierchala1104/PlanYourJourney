package com.example.planyourjourney.feature_planing.presentation.settings

import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.Settings

data class SettingsState (
    val isLoading: Boolean = false,
    val isSettingsLoaded: Boolean = false,
    val settings: Settings = Settings(),
    val locationList: List<Location> = listOf(),
)