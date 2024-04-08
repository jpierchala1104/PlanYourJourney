package com.example.planyourjourney.feature_planing.presentation.weather_details

import com.example.planyourjourney.feature_planing.presentation.util.WeatherVariables


sealed class WeatherDetailsEvent {
    data class WeatherVariablesChanged(val weatherVariables: WeatherVariables) : WeatherDetailsEvent()
    data object ToggleWeatherVariablesSection : WeatherDetailsEvent()
}
