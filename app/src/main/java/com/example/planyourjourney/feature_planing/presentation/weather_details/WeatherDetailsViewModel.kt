package com.example.planyourjourney.feature_planing.presentation.weather_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.use_case.WeatherDetailsUseCases
import com.example.planyourjourney.feature_planing.domain.util.Resource
import com.example.planyourjourney.feature_planing.presentation.planning.PlaningViewModel
import com.example.planyourjourney.feature_planing.presentation.util.ChartService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val weatherDetailsUseCases: WeatherDetailsUseCases
) : ViewModel() {
    private val _state = mutableStateOf(WeatherDetailsState())
    val state: State<WeatherDetailsState> = _state

    private val uiEventChannel = Channel<PlaningViewModel.UiEvent>()
    val uiEvents = uiEventChannel.receiveAsFlow()

    private val chartService = ChartService()

    init {
        getSettings()
        viewModelScope.launch {
            val locationId = savedStateHandle.get<Int>("locationId") ?: return@launch
            getLocationWeather(
                Location(
                    locationId = locationId,
                    locationName = null,
                    coordinates = Coordinates(latitude = 0.0, longitude = 0.0)
                )
            )
        }
    }

    fun onEvent(event: WeatherDetailsEvent) {
        when (event) {
            is WeatherDetailsEvent.WeatherVariablesChanged -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        weatherVariables = event.weatherVariables
                    )
                    if (_state.value.isWeatherLoaded)
                        prepareCharts()
                }
            }

            WeatherDetailsEvent.ToggleWeatherVariablesSection -> {
                _state.value = state.value.copy(
                    isWeatherVariablesSectionVisible = !state.value.isWeatherVariablesSectionVisible
                )
            }
        }
    }

    private fun getLocationWeather(location: Location) {
        viewModelScope.launch {
            weatherDetailsUseCases.getWeatherAtLocationUseCase.invoke(
                location
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { locationWeather ->
                            _state.value = state.value.copy(
                                locationWeather = locationWeather
                            )
                        }
//                        _state.value = _state.value.copy(
//                            isWeatherLoaded = true, isLoading = false
//                        )
                        prepareCharts()
                        uiEventChannel.send(PlaningViewModel.UiEvent.LocationsLoaded)
                    }

                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isWeatherLoaded = false, isLoading = false
                        )
                        uiEventChannel.send(PlaningViewModel.UiEvent.LoadingError(result.message!!))
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    private fun prepareCharts() {
        var chartStateList: List<ChartState> = listOf()
        if (_state.value.locationWeather == null) return
        val hourlyWeatherList = _state.value.locationWeather!!.hourlyWeatherList
        if (_state.value.weatherVariables.isTemperature2mChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Temperature",
                    modelProducer = chartService.getCartesianLineChartModelProducer(chartSize = hourlyWeatherList.size,
                        chartDoubleValues = hourlyWeatherList.map { it.temperature2m }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.temperatureUnits.displayUnits
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.temperatureUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = hourlyWeatherList
                    )
                )
            )
        }
        if (_state.value.weatherVariables.isRelativeHumidity2mChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Relative Humidity",
                    modelProducer = chartService.getCartesianLineChartModelProducer(chartSize = hourlyWeatherList.size,
                        chartIntValues = hourlyWeatherList.map { it.relativeHumidity2m }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = hourlyWeatherList
                    )
                )
            )
        }
        if (_state.value.weatherVariables.isPrecipitationProbabilityChecked) {
            chartStateList =
                chartStateList.plus(
                    ChartState(
                        chartTitle = "Precipitation Probability",
                        modelProducer = chartService.getCartesianLineChartModelProducer(chartSize = hourlyWeatherList.size,
                            chartIntValues = hourlyWeatherList.map { it.precipitationProbability }),
                        markerLabelFormatter = chartService.getMarkerLabelFormatter(
                            hourlyWeather = hourlyWeatherList,
                            unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                        ),
                        startAxisValueFormatter = chartService.getStartAxisFormatter(
                            unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                        ),
                        bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                            hourlyWeather = hourlyWeatherList
                        )
                    )
                )
        }
        if (_state.value.weatherVariables.isRainChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Rain",
                    modelProducer = chartService.getCartesianColumnChartModelProducer(chartSize = hourlyWeatherList.size,
                        chartDoubleValues = hourlyWeatherList.map { it.rain }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits,
                        isLineCartesianChart = false
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = hourlyWeatherList
                    )
                )
            )
        }
        if (_state.value.weatherVariables.isSnowfallChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Snowfall",
                    modelProducer = chartService.getCartesianColumnChartModelProducer(chartSize = hourlyWeatherList.size,
                        chartDoubleValues = hourlyWeatherList.map { it.snowfall }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits,
                        isLineCartesianChart = false
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = hourlyWeatherList
                    )
                )
            )
        }
        if (_state.value.weatherVariables.isCloudCoverChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Cloud Cover",
                    modelProducer = chartService.getCartesianLineChartModelProducer(chartSize = hourlyWeatherList.size,
                        chartIntValues = hourlyWeatherList.map { it.cloudCover }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = hourlyWeatherList
                    )
                )
            )
        }
        if (_state.value.weatherVariables.isWindSpeed10mChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Wind Speed at 10 meters",
                    modelProducer = chartService.getCartesianLineChartModelProducer(chartSize = hourlyWeatherList.size,
                        chartDoubleValues = hourlyWeatherList.map { it.windSpeed10m }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.windSpeedUnits.displayUnits
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.windSpeedUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = hourlyWeatherList
                    )
                )
            )
        }
        _state.value = state.value.copy(
            chartStateList = chartStateList, isWeatherLoaded = true, isLoading = false
        )
    }

    private fun getSettings(){
        viewModelScope.launch {
            weatherDetailsUseCases.getSettingsUseCase.invoke().collect{ settings ->
                _state.value = state.value.copy(
                    settings = settings
                )
            }
        }
    }
}