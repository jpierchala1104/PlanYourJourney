package com.example.planyourjourney.feature_planing.presentation.settings

import com.example.planyourjourney.feature_planing.domain.model.Settings

sealed class SettingsEvent {
//    data object SaveSettings : SettingsEvent()
    data class SettingsChanged(val settings: Settings) : SettingsEvent()
}