package com.example.planyourjourney.feature_planing.presentation.planning

import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.presentation.util.Language
import com.example.planyourjourney.feature_planing.presentation.util.OutputType
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType
import com.example.planyourjourney.feature_planing.presentation.util.WeatherVariables
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.marker.DefaultMarkerLabelFormatter
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer

data class PlanningState(
    val locationList: List<Location> = listOf(),
    val isLoading: Boolean = false,
    val isLocationLoaded: Boolean = false,
    val isCoordinatesValueOrLocationNameChanged: Boolean = false,
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