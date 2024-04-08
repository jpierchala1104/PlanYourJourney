package com.example.planyourjourney.feature_planing.presentation.weather_details

import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
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

data class WeatherDetailsState(
    val locationWeather: LocationWeather? = null,
    val isLoading: Boolean = false,
    val isWeatherLoaded: Boolean = false,
    val weatherVariables: WeatherVariables = WeatherVariables(
        isTemperature2mChecked = true,
        isRelativeHumidity2mChecked = false,
        isPrecipitationProbabilityChecked = false,
        isRainChecked = false,
        isSnowfallChecked = false,
        isCloudCoverChecked = false,
        isWindSpeed10mChecked = false
    ),
    val isWeatherVariablesSectionVisible: Boolean = false,
    val settings: Settings = Settings(
        // language = Language. -> English and Polski (for polish)
        language = Language.English,
        // searchInputType = SearchInputType. -> LocationName, LatitudeAndLongitude and GoogleMaps
        searchInputType = SearchInputType.LocationName,
        // outputType = OutputType. -> Chart and Card
        outputType = OutputType.Chart,
        // weatherUnits = TemperatureUnits, PrecipitationUnits, WindSpeedUnits and Percentages
        weatherUnits = WeatherUnits()
    ),
    val chartStateList: List<ChartState> = listOf()
)

data class ChartState(
    val chartTitle: String,
    val modelProducer: CartesianChartModelProducer,
    val markerLabelFormatter: MarkerLabelFormatter = DefaultMarkerLabelFormatter(),
    val startAxisValueFormatter: AxisValueFormatter<AxisPosition.Vertical.Start>,
    val bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>
)