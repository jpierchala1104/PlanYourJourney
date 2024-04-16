package com.example.planyourjourney.feature_planing.presentation.settings

import com.example.planyourjourney.feature_planing.domain.model.Settings

sealed class SettingsEvent {
    data class SettingsSaved(val settings: Settings) : SettingsEvent()
}