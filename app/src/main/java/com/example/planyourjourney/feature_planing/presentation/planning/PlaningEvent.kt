package com.example.planyourjourney.feature_planing.presentation.planning

import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.presentation.util.WeatherVariables

sealed class PlaningEvent {
    //data class WeatherVariablesChange(val weatherVariables: WeatherVariables) : PlaningEvent()
    object GetWeather : PlaningEvent()
    data class LocationNameChanged(val newLocationNameText: String) : PlaningEvent()
    data class CoordinatesChanged(val newCoordinates: Coordinates) : PlaningEvent()
    //object ToggleSettingsSection : PlaningEvent()
}
