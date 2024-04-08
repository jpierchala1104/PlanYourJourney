package com.example.planyourjourney.feature_planing.presentation.weather

import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.presentation.util.Language
import com.example.planyourjourney.feature_planing.presentation.util.OutputType
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType

data class WeatherState (
    val locationWeatherList: List<LocationWeather> = listOf(),
    val isLoading: Boolean = false,
    val isWeatherLoaded: Boolean = false,
    val settings: Settings = Settings(
        // language = Language. -> English and Polski (for polish)
        language = Language.English,
        // searchInputType = SearchInputType. -> LocationName, LatitudeAndLongitude and GoogleMaps
        searchInputType = SearchInputType.LocationName,
        // outputType = OutputType. -> Chart and Card
        outputType = OutputType.Chart,
        // weatherUnits = TemperatureUnits, PrecipitationUnits, WindSpeedUnits and Percentages
        weatherUnits = WeatherUnits()
    )
)