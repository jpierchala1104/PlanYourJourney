package com.example.planyourjourney.feature_planing.presentation.weather


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.use_case.WeatherUseCases
import com.example.planyourjourney.feature_planing.domain.util.APIErrorResult
import com.example.planyourjourney.feature_planing.domain.util.APIFetchResult
import com.example.planyourjourney.feature_planing.domain.util.Resource
import com.example.planyourjourney.feature_planing.presentation.planning.PlaningViewModel
import com.example.planyourjourney.feature_planing.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCases: WeatherUseCases
) : ViewModel() {
    private val _state = mutableStateOf(WeatherState())
    val state: State<WeatherState> = _state

    private var recentlyDeletedLocation: Location? = null

    private val uiEventChannel = Channel<UiEvent>()
    val uiEvents = uiEventChannel.receiveAsFlow()

    init {
        clearOldWeather()
        getSettings()
        getLocationsWithWeather()
    }

    fun onEvent(event: WeatherEvent){
        when(event){
            is WeatherEvent.RefreshWeather -> {
                // TODO: refresh weather thingy in repository
            }
            is WeatherEvent.DeleteLocation -> {
                viewModelScope.launch {
                    weatherUseCases.deleteWeatherAtLocationUseCase(event.location)
                    weatherUseCases.deleteLocationUseCase(event.location)
                    recentlyDeletedLocation = event.location
                    getLocationsWithWeather()
                }
            }
            is WeatherEvent.RestoreLocation -> {
                viewModelScope.launch {
                    weatherUseCases
                        .insertLocationUseCase(location = recentlyDeletedLocation ?: return@launch)
                    weatherUseCases
                        .fetchWeatherAtLocationUseCase(
                            location = recentlyDeletedLocation ?: return@launch,
                            weatherUnits = _state.value.settings.weatherUnits
                        )
                    recentlyDeletedLocation = null
                    getLocationsWithWeather()
                }
            }

            is WeatherEvent.RefreshLocationWeather -> {
                fetchFromAPI(event.location)
            }
        }
    }

    private fun getLocationsWithWeather(){
        viewModelScope.launch {
            weatherUseCases.getLocationsWithWeatherUseCase.invoke()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { weather ->
                                _state.value = state.value.copy(
                                    locationWeatherList = weather
                                )
                            }
                            //uiEventChannel.send(UiEvent.WeatherLoaded)
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

    private fun fetchFromAPI(location: Location) {
        viewModelScope.launch {
            weatherUseCases.fetchWeatherAtLocationUseCase.invoke(
                location,
                _state.value.settings.weatherUnits
            ).collect { result ->
                when (result) {
                    is APIFetchResult.Success -> {
                        // TODO: can revisit this problem

                        // Refresh LocationWeather list on success
                        getLocationsWithWeather()
                    }

                    is APIFetchResult.Error -> {
                        when(result.apiErrorResult){
                            APIErrorResult.DataLoadError -> {
                                uiEventChannel.send(UiEvent.LoadingError(R.string.api_request_error_else))
                            }
                            APIErrorResult.HttpExceptionError -> {
                                uiEventChannel.send(UiEvent.LoadingError(R.string.api_request_error_http))
                            }
                            APIErrorResult.IOExceptionError -> {
                                uiEventChannel.send(UiEvent.LoadingError(R.string.api_request_error_io))
                            }
                            else -> {
                                //Would only be if its null, there is no chance of null here
                            }
                        }
                    }

                    is APIFetchResult.Loading -> {
                        // TODO:
                    }
                }
            }
        }
    }

    private fun clearOldWeather(){
        viewModelScope.launch {
            weatherUseCases.clearOldWeatherUseCase.invoke()
        }
    }

    private fun getSettings(){
        viewModelScope.launch {
            weatherUseCases.getSettingsUseCase.invoke().collect{ settings ->
                _state.value = state.value.copy(
                    settings = settings
                )
            }
        }
    }
}