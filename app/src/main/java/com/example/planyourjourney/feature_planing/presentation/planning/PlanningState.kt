package com.example.planyourjourney.feature_planing.presentation.planning

import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.util.Language
import com.example.planyourjourney.feature_planing.domain.util.OutputType
import com.example.planyourjourney.feature_planing.domain.util.SearchInputType
import com.example.planyourjourney.feature_planing.presentation.util.WeatherVariables
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.marker.DefaultMarkerLabelFormatter
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer

data class PlanningState(
//    val coordinates: Coordinates = Coordinates(0.0, 0.0),
    val hourlyWeatherList: List<HourlyWeather> = listOf(),
    val isLoading: Boolean = false,
    val isWeatherLoaded: Boolean = false,
    val isCoordinatesValueOrLocationNameChanged: Boolean = false,
//    val locationName: String = "",
    val weatherVariables: WeatherVariables = WeatherVariables(
        isTemperature2mChecked = true,
        isRelativeHumidity2mChecked = false,
        isPrecipitationProbabilityChecked = false,
        isRainChecked = false,
        isSnowfallChecked = false,
        isCloudCoverChecked = false,
        isWindSpeed10mChecked = false
    ),
    val settings: Settings = Settings(
        // language = Language. -> English and Polski (for polish)
        language = Language.English,
        // searchInputType = SearchInputType. -> LocationName, LatitudeAndLongitude and GoogleMaps
        searchInputType = SearchInputType.LocationName,
        // outputType = OutputType. -> Chart and Card
        outputType = OutputType.Chart
    ),
    val chartStateList: List<ChartState> = listOf()
//    val isSettingsSectionVisible: Boolean = false
)

data class ChartState (
    val chartTitle: String,
    val modelProducer: CartesianChartModelProducer,
    val markerLabelFormatter: MarkerLabelFormatter = DefaultMarkerLabelFormatter(),
    val startAxisValueFormatter: AxisValueFormatter<AxisPosition.Vertical.Start>,
    val bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>
)
