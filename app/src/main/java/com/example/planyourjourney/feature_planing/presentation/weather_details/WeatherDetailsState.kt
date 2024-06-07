package com.example.planyourjourney.feature_planing.presentation.weather_details

import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.model.WeatherVariables
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.marker.DefaultMarkerLabelFormatter
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer

data class WeatherDetailsState(
    val locationWeather: LocationWeather? = null,
    val isLoading: Boolean = false,
    val isWeatherLoaded: Boolean = false,
    val isWeatherVariablesSectionVisible: Boolean = false,
    val settings: Settings = Settings(),
    val chartStateList: List<ChartState> = listOf()
)

data class ChartState(
    val chartTitleResourceId: Int,
    val modelProducer: CartesianChartModelProducer,
    val markerLabelFormatter: MarkerLabelFormatter = DefaultMarkerLabelFormatter(),
    val startAxisValueFormatter: AxisValueFormatter<AxisPosition.Vertical.Start>,
    val bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>
)