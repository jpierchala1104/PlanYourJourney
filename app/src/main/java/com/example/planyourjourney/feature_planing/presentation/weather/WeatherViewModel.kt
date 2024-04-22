package com.example.planyourjourney.feature_planing.presentation.weather


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.use_case.WeatherUseCases
import com.example.planyourjourney.feature_planing.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCases: WeatherUseCases
) : ViewModel() {
    private val _state = mutableStateOf(WeatherState())
    val state: State<WeatherState> = _state

    private var recentlyDeletedLocation: Location? = null


    init {
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
                            //uiEventChannel.send(UiEvent.LoadingError(result.message!!))
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(isLoading = result.isLoading)
                        }
                    }
                }
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