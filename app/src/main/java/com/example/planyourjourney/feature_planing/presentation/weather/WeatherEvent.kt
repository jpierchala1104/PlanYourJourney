package com.example.planyourjourney.feature_planing.presentation.weather

import com.example.planyourjourney.feature_planing.domain.model.Location

sealed class WeatherEvent{
    data class DeleteLocation(val location: Location) : WeatherEvent()
    data object RefreshWeather : WeatherEvent() // TODO: Need to think how to trigger API requests
    // without sending 10 requests at once
    data class RefreshLocationWeather(val location: Location) : WeatherEvent()
    data object RestoreLocation : WeatherEvent()
}
