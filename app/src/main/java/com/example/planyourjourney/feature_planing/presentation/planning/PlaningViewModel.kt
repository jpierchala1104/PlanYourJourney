package com.example.planyourjourney.feature_planing.presentation.planning

import android.app.Application
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.feature_planing.presentation.util.ChartService
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.util.SearchInputType
import com.example.planyourjourney.feature_planing.domain.use_case.GetWeatherUseCase
import com.example.planyourjourney.feature_planing.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaningViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase, application: Application
) : AndroidViewModel(application) {
    private val context = application

    private val _state = mutableStateOf(PlanningState())
    val state: State<PlanningState> = _state

//    private val _weatherLatitude = mutableStateOf("")
//    val weatherLatitude: State<String> = _weatherLatitude
//
//    private val _weatherLongitude = mutableStateOf("")
//    val weatherLongitude: State<String> = _weatherLongitude

    private val _weatherCoordinates = mutableStateOf(Coordinates(0.0, 0.0))
    val weatherCoordinates: State<Coordinates> = _weatherCoordinates

    private val _weatherLocationName = mutableStateOf("")
    val weatherLocationName: State<String> = _weatherLocationName

    private val uiEventChannel = Channel<UiEvent>()
    val uiEvents = uiEventChannel.receiveAsFlow()

    private val geocoder = Geocoder(context)

    private val chartService = ChartService()

    // TODO: List of locations, another screen for showing (or a list of locations with basic info like today's weather card) charts or cards,
    //  and another screen for changing settings
    //  DataStores for saving settings and RoomDatabase for  storing weather.
    //  Could also make another screen for showing just the charts of one location when picking a location

    // TODO: state should have only location and coords, list of hourly weather should be in location list or specific location weather info.
    //  need to figure out where the API request gonna happen

    fun onEvent(event: PlaningEvent) {
        when (event) {
            //is PlaningEvent.WeatherVariablesChange -> TODO()
            is PlaningEvent.GetWeather -> {
                if (!_state.value.isCoordinatesValueOrLocationNameChanged && _state.value.hourlyWeatherList.isEmpty()) return

                _state.value = _state.value.copy(
                    isWeatherLoaded = false, isLoading = true
                )
                when (_state.value.settings.searchInputType) {
                    SearchInputType.LatitudeAndLongitude -> {
                        getWeather()
                    }

                    SearchInputType.LocationName -> {
                        getCoordinatesFromLocationName()
                        if (android.os.Build.VERSION.SDK_INT < 33) {
                            getWeather()
                        }
                    }
                }
                _state.value = _state.value.copy(
                    isCoordinatesValueOrLocationNameChanged = false
                )
            }

            is PlaningEvent.CoordinatesChanged -> {
                viewModelScope.launch {
                    _weatherCoordinates.value = _weatherCoordinates.value.copy(
                        latitude = event.newCoordinates.latitude,
                        longitude = event.newCoordinates.longitude,
                    )
                    _state.value = _state.value.copy(
                        isCoordinatesValueOrLocationNameChanged = true
                    )
//                    _state.value = state.value.copy(
//                        coordinates = event.newCoordinates
//                    )
                }
            }

            is PlaningEvent.LocationNameChanged -> {
                viewModelScope.launch {
                    _weatherLocationName.value = event.newLocationNameText
                    _state.value = _state.value.copy(
                        isCoordinatesValueOrLocationNameChanged = true
                    )
//                    _state.value = state.value.copy(
//                        locationName = event.newLocationNameText
//                    )
                }
            }
//            is PlaningEvent.ToggleSettingsSection -> TODO()
        }
    }

    private fun getWeather() {
        viewModelScope.launch {
            getWeatherUseCase.invoke(_weatherCoordinates.value, _state.value.settings.weatherUnits).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { weather ->
                            _state.value = _state.value.copy(
                                hourlyWeatherList = weather
                            )
                        }
                        prepareCharts()
                        uiEventChannel.send(UiEvent.WeatherLoaded)
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isWeatherLoaded = false, isLoading = false
                        )
                        uiEventChannel.send(UiEvent.LoadingError(result.message!!))
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getCoordinatesFromLocationName() {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            geocoder.getFromLocationName(_weatherLocationName.value, 1) { coords ->
                _weatherCoordinates.value.latitude = coords.first().latitude
                _weatherCoordinates.value.longitude = coords.first().longitude
                getWeather()
            }
            return
        }
        val coords = geocoder.getFromLocationName(_weatherLocationName.value, 1)
        if (!coords.isNullOrEmpty()) {
            _weatherCoordinates.value.latitude = coords.first().latitude
            _weatherCoordinates.value.longitude = coords.first().longitude
        } else {
            Log.e("Geocoder", "Geocoder didn't find location")
        }
    }

    private fun prepareCharts() {
        var chartStateList: List<ChartState> = listOf()
        if (_state.value.weatherVariables.isTemperature2mChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Temperature",
                    modelProducer = chartService.getCartesianLineChartModelProducer(chartSize = _state.value.hourlyWeatherList.size,
                        chartDoubleValues = _state.value.hourlyWeatherList.map { it.temperature2m }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.temperatureUnits.displayUnits
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.temperatureUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList
                    )
                )
            )
        }
        if (_state.value.weatherVariables.isRelativeHumidity2mChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Relative Humidity",
                    modelProducer = chartService.getCartesianLineChartModelProducer(chartSize = _state.value.hourlyWeatherList.size,
                        chartIntValues = _state.value.hourlyWeatherList.map { it.relativeHumidity2m }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList
                    )
                )
            )
        }
        if (_state.value.weatherVariables.isPrecipitationProbabilityChecked) {
            chartStateList =
                chartStateList.plus(
                    ChartState(
                        chartTitle = "Precipitation Probability",
                        modelProducer = chartService.getCartesianLineChartModelProducer(chartSize = _state.value.hourlyWeatherList.size,
                            chartIntValues = _state.value.hourlyWeatherList.map { it.precipitationProbability }),
                        markerLabelFormatter = chartService.getMarkerLabelFormatter(
                            hourlyWeather = _state.value.hourlyWeatherList,
                            unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                        ),
                        startAxisValueFormatter = chartService.getStartAxisFormatter(
                            unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                        ),
                        bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                            hourlyWeather = _state.value.hourlyWeatherList
                        )
                    )
                )
        }
        if (_state.value.weatherVariables.isRainChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Rain",
                    modelProducer = chartService.getCartesianColumnChartModelProducer(chartSize = _state.value.hourlyWeatherList.size,
                        chartDoubleValues = _state.value.hourlyWeatherList.map { it.rain }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList
                    )
                )
            )
        }
        if (_state.value.weatherVariables.isSnowfallChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Snowfall",
                    modelProducer = chartService.getCartesianColumnChartModelProducer(chartSize = _state.value.hourlyWeatherList.size,
                        chartDoubleValues = _state.value.hourlyWeatherList.map { it.snowfall }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.precipitationUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList
                    )
                )
            )
        }
        if (_state.value.weatherVariables.isCloudCoverChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Cloud Cover",
                    modelProducer = chartService.getCartesianLineChartModelProducer(chartSize = _state.value.hourlyWeatherList.size,
                        chartIntValues = _state.value.hourlyWeatherList.map { it.cloudCover }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.percentageUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList
                    )
                )
            )
        }
        if (_state.value.weatherVariables.isWindSpeed10mChecked) {
            chartStateList = chartStateList.plus(
                ChartState(
                    chartTitle = "Wind Speed at 10 meters",
                    modelProducer = chartService.getCartesianLineChartModelProducer(chartSize = _state.value.hourlyWeatherList.size,
                        chartDoubleValues = _state.value.hourlyWeatherList.map { it.windSpeed10m }),
                    markerLabelFormatter = chartService.getMarkerLabelFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList,
                        unit = _state.value.settings.weatherUnits.windSpeedUnits.displayUnits
                    ),
                    startAxisValueFormatter = chartService.getStartAxisFormatter(
                        unit = _state.value.settings.weatherUnits.windSpeedUnits.displayUnits
                    ),
                    bottomAxisValueFormatter = chartService.getBottomAxisFormatter(
                        hourlyWeather = _state.value.hourlyWeatherList
                    )
                )
            )
        }
        _state.value = _state.value.copy(
            chartStateList = chartStateList, isWeatherLoaded = true, isLoading = false
        )
    }

    sealed class UiEvent {
        object WeatherLoaded : UiEvent()
        data class LoadingError(val message: String) : UiEvent()
    }
}

