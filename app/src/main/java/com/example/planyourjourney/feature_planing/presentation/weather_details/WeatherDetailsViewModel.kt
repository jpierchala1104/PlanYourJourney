package com.example.planyourjourney.feature_planing.presentation.weather_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.use_case.WeatherDetailsUseCases
import com.example.planyourjourney.feature_planing.domain.util.Resource
import com.example.planyourjourney.feature_planing.presentation.planning.PlaningViewModel
import com.example.planyourjourney.feature_planing.presentation.util.ChartService
import com.example.planyourjourney.feature_planing.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val weatherDetailsUseCases: WeatherDetailsUseCases
) : ViewModel() {

    private val _state = mutableStateOf(WeatherDetailsState())
    val state: State<WeatherDetailsState> = _state

    private val uiEventChannel = Channel<UiEvent>()
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
                        settings = state.value
                            .settings.copy(weatherVariables = event.weatherVariables)
                    )
                    weatherDetailsUseCases.saveSettingsUseCase.invoke(_state.value.settings)
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
                        uiEventChannel.send(UiEvent.LocationsLoaded)
                    }

                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isWeatherLoaded = false, isLoading = false
                        )
                        uiEventChannel.send(UiEvent.LoadingError(R.string.dao_request_error))
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    private fun prepareCharts() {
        chartService.setLocale(Locale(_state.value.settings.language.localeCode))

        var chartStateList: List<ChartState> = listOf()
        if (_state.value.locationWeather == null) return
        val hourlyWeatherList = _state.value.locationWeather!!.hourlyWeatherList
        if (hourlyWeatherList.isEmpty()) {
            _state.value = state.value.copy(
                isWeatherLoaded = false, isLoading = false
            )
            return
        }
        if (_state.value.settings.weatherVariables.isTemperature2mChecked) {
            chartStateList = chartStateList.plus(
                chartService.getLineChartState(
                    chartTitleResourceId = R.string.temperature,
                    chartValuesList = hourlyWeatherList.map { it.temperature2m },
                    localDateTimeList = hourlyWeatherList.map { it.time },
                    unit = _state.value.settings.weatherUnits.temperatureUnits.displayUnits
                )
            )
        }
        if (_state.value.settings.weatherVariables.isPrecipitationProbabilityChecked) {
            chartStateList = chartStateList.plus(
                chartService.getLineChartState(
                    chartTitleResourceId = R.string.precipitation_probability,
                    chartValuesList = hourlyWeatherList.map { it.precipitationProbability.toDouble() },
                    localDateTimeList = hourlyWeatherList.map { it.time },
                    unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                )
            )
        }
        if (_state.value.settings.weatherVariables.isPrecipitationChecked) {
            chartStateList = chartStateList.plus(
                chartService.getColumnChartState(
                    chartTitleResourceId = R.string.precipitation,
                    chartValuesList = hourlyWeatherList.map { it.precipitation },
                    localDateTimeList = hourlyWeatherList.map { it.time },
                    unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits
                )
            )
        }
        if (_state.value.settings.weatherVariables.isRainChecked) {
            chartStateList = chartStateList.plus(
                chartService.getColumnChartState(
                    chartTitleResourceId = R.string.rain,
                    chartValuesList = hourlyWeatherList.map { it.rain },
                    localDateTimeList = hourlyWeatherList.map { it.time },
                    unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits
                )
            )
        }
        if (_state.value.settings.weatherVariables.isSnowfallChecked) {
            chartStateList = chartStateList.plus(
                chartService.getColumnChartState(
                    chartTitleResourceId = R.string.snowfall,
                    chartValuesList = hourlyWeatherList.map { it.snowfall },
                    localDateTimeList = hourlyWeatherList.map { it.time },
                    unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits
                )
            )
        }
        if (_state.value.settings.weatherVariables.isCloudCoverChecked) {
            chartStateList = chartStateList.plus(
                chartService.getLineChartState(
                    chartTitleResourceId = R.string.cloud_cover,
                    chartValuesList = hourlyWeatherList.map { it.cloudCover.toDouble() },
                    localDateTimeList = hourlyWeatherList.map { it.time },
                    unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                )
            )
        }
        if (_state.value.settings.weatherVariables.isRelativeHumidity2mChecked) {
            chartStateList = chartStateList.plus(
                chartService.getLineChartState(
                    chartTitleResourceId = R.string.relative_humidity,
                    chartValuesList = hourlyWeatherList.map { it.relativeHumidity2m.toDouble() },
                    localDateTimeList = hourlyWeatherList.map { it.time },
                    unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                )
            )
        }
        if (_state.value.settings.weatherVariables.isWindSpeed10mChecked) {
            chartStateList = chartStateList.plus(
                chartService.getLineChartState(
                    chartTitleResourceId = R.string.wind_speed_at_10_m,
                    chartValuesList = hourlyWeatherList.map { it.windSpeed10m },
                    localDateTimeList = hourlyWeatherList.map { it.time },
                    unit = _state.value.settings.weatherUnits.windSpeedUnits.displayUnits
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
                prepareCharts()
            }
        }
    }
}