package com.example.planyourjourney.feature_planing.presentation.planning

import android.app.Application
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.feature_planing.presentation.util.ChartService
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.use_case.PlaningUseCases
import com.example.planyourjourney.feature_planing.domain.util.APIFetchResult
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType
import com.example.planyourjourney.feature_planing.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaningViewModel @Inject constructor(
    private val planingUseCases: PlaningUseCases,
    application: Application
) : AndroidViewModel(application) {
    private val context = application

    private val _state = mutableStateOf(PlanningState())
    val state: State<PlanningState> = _state

    private val _weatherCoordinates = mutableStateOf(Coordinates(0.0, 0.0))
    val weatherCoordinates: State<Coordinates> = _weatherCoordinates

    private val _weatherLocationName = mutableStateOf("")
    val weatherLocationName: State<String> = _weatherLocationName

    private val uiEventChannel = Channel<UiEvent>()
    val uiEvents = uiEventChannel.receiveAsFlow()

    private var recentlyDeletedLocation: Location? = null

    private val geocoder = Geocoder(context)


    // Listening for location change then saving the new location
    private val geocodeListener = Geocoder.GeocodeListener { coords ->
        _weatherCoordinates.value.latitude = coords.first().latitude
        _weatherCoordinates.value.longitude = coords.first().longitude
        val location = Location(
            locationId = null,
            locationName = weatherLocationName.value,
            coordinates = weatherCoordinates.value
        )
        saveLocation(location)
        fetchFromAPI(location)
        getLocations()
    }

    // TODO: List of locations, another screen for showing (or a list of locations with basic info like today's weather card) charts or cards,
    //  and another screen for changing settings
    //  DataStores for saving settings and RoomDatabase for  storing weather.
    //  Could also make another screen for showing just the charts of one location when picking a location

    // TODO: can make a unit converter, wouldn't need to call the API for every unit change

    init {
        getSettings()
        getLocations()
    }

    fun onEvent(event: PlaningEvent) {
        when (event) {
            //is PlaningEvent.WeatherVariablesChange -> TODO()
            is PlaningEvent.AddLocation -> {
                if (!_state.value.isCoordinatesValueOrLocationNameChanged) return

                _state.value = state.value.copy(
                    isLoading = true
                )

                if (_state.value.searchInputType == SearchInputType.LocationName) {
                    getCoordinatesFromLocationName()
                }

                if (Build.VERSION.SDK_INT < 33 ||
                    _state.value.searchInputType == SearchInputType.LatitudeAndLongitude
                ) {
                    val location = Location(
                        locationId = null,
                        locationName = weatherLocationName.value.ifEmpty { null },
                        coordinates = weatherCoordinates.value
                    )
                    saveLocation(location)
                    fetchFromAPI(location)
                    // TODO: need to figure out how to return if fetchFromAPI Error
                    getLocations()
                }

                _state.value = state.value.copy(
                    isCoordinatesValueOrLocationNameChanged = false
                )
            }

            is PlaningEvent.CoordinatesChanged -> {
                viewModelScope.launch {
                    _weatherCoordinates.value = weatherCoordinates.value.copy(
                        latitude = event.newCoordinates.latitude,
                        longitude = event.newCoordinates.longitude,
                    )
                    _state.value = state.value.copy(
                        isCoordinatesValueOrLocationNameChanged = true
                    )
                }
            }

            is PlaningEvent.LocationNameChanged -> {
                viewModelScope.launch {
                    _weatherLocationName.value = event.newLocationNameText
                    _state.value = state.value.copy(
                        isCoordinatesValueOrLocationNameChanged = true
                    )
                }
            }

            is PlaningEvent.DeleteLocation -> {
                viewModelScope.launch {
                    planingUseCases.deleteWeatherAtLocationUseCase(event.location)
                    planingUseCases.deleteLocationUseCase(event.location)
                    recentlyDeletedLocation = event.location
                    getLocations()
                }
            }

            is PlaningEvent.RestoreLocation -> {
                viewModelScope.launch {
                    saveLocation(recentlyDeletedLocation ?: return@launch)
                    fetchFromAPI(recentlyDeletedLocation ?: return@launch)
                    recentlyDeletedLocation = null
                    getLocations()
                }
            }

            is PlaningEvent.SearchInputTypeChanged -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        searchInputType = event.searchInputType
                    )
                }
            }
        }
    }

    private fun saveLocation(location: Location) {
        viewModelScope.launch {
            if (_state.value.locationList
                    .any {
                        (it.coordinates.latitude == location.coordinates.latitude &&
                                it.coordinates.longitude == location.coordinates.longitude) ||
                                it.locationName == location.locationName
                    }
            )
                return@launch
            planingUseCases.insertLocationUseCase(location)
        }
    }

    private fun fetchFromAPI(location: Location) {
        viewModelScope.launch {
            planingUseCases.fetchWeatherAtLocationUseCase.invoke(
                location,
                _state.value.settings.weatherUnits
            ).collect { result ->
                when (result) {
                    is APIFetchResult.Success -> {
                        val locationListCopy = state.value.locationList.toList()
                        locationListCopy
                            .first {
                                it.coordinates.latitude == location.coordinates.latitude &&
                                        it.coordinates.longitude == location.coordinates.longitude
                            }.isLoaded = true
                        _state.value = state.value.copy(
                            locationList = locationListCopy
                        )
                    }

                    is APIFetchResult.Error -> {
                        val locationListCopy = state.value.locationList.toList()
                        locationListCopy
                            .first {
                                it.coordinates.latitude == location.coordinates.latitude &&
                                        it.coordinates.longitude == location.coordinates.longitude
                            }.isLoaded = false
                        _state.value = state.value.copy(
                            locationList = locationListCopy
                        )
                        uiEventChannel.send(UiEvent.LoadingError(result.message!!))
                    }

                    is APIFetchResult.Loading -> {
                        val locationListCopy = state.value.locationList.toList()
                        locationListCopy
                            .first {
                                it.coordinates.latitude == location.coordinates.latitude &&
                                        it.coordinates.longitude == location.coordinates.longitude
                            }.isLoaded = result.isLoading
                        _state.value = state.value.copy(
                            locationList = locationListCopy
                        )
                    }
                }
            }
        }
    }

    private fun getLocations() {
        viewModelScope.launch {
            planingUseCases.getLocationsUseCase.invoke()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { locations ->
                                _state.value = state.value.copy(
                                    locationList = locations
                                )
                            }
                            _state.value = state.value.copy(
                                isLocationLoaded = true, isLoading = false
                            )
                            uiEventChannel.send(UiEvent.LocationsLoaded)
                        }

                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                isLocationLoaded = false, isLoading = false
                            )
                            uiEventChannel.send(UiEvent.LoadingError(result.message!!))
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    @Suppress("DEPRECATION")
    private fun getCoordinatesFromLocationName() {
        if (Build.VERSION.SDK_INT >= 33) {
            geocoder.getFromLocationName(_weatherLocationName.value, 1, geocodeListener)
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

    private fun getSettings(){
        viewModelScope.launch {
            planingUseCases.getSettingsUseCase.invoke().collect{ settings ->
                _state.value = state.value.copy(
                    settings = settings
                )
            }
        }
    }

    sealed class UiEvent {
        data object LocationsLoaded : UiEvent()
        data class LoadingError(val message: String) : UiEvent()
    }
}
