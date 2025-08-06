package com.example.planyourjourney.feature_planing.presentation.planning

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.use_case.PlaningUseCases
import com.example.planyourjourney.feature_planing.domain.util.APIErrorResult
import com.example.planyourjourney.feature_planing.domain.util.APIFetchResult
import com.example.planyourjourney.feature_planing.domain.util.Resource
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType
import com.example.planyourjourney.feature_planing.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlanningViewModel @Inject constructor(
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
        getLocations()
        fetchFromAPI(location)
    }

    // TODO: can take localization info from the phone to show date time in localized style

    // TODO: figure out how and when edit/call and save updates from weather API


    init {
        getSettings()
        getLocations()
    }

    fun onEvent(event: PlanningEvent) {
        when (event) {
            is PlanningEvent.AddLocation -> {
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
                    getLocations()
                    fetchFromAPI(location)
                }

                _state.value = state.value.copy(
                    isCoordinatesValueOrLocationNameChanged = false
                )
            }

            is PlanningEvent.CoordinatesChanged -> {
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

            is PlanningEvent.LocationNameChanged -> {
                viewModelScope.launch {
                    _weatherLocationName.value = event.newLocationNameText
                    _state.value = state.value.copy(
                        isCoordinatesValueOrLocationNameChanged = true
                    )
                }
            }

            is PlanningEvent.DeleteLocation -> {
                viewModelScope.launch {
                    planingUseCases.deleteWeatherAtLocationUseCase(event.location)
                    planingUseCases.deleteLocationUseCase(event.location)
                    recentlyDeletedLocation = event.location
                    getLocations()
                }
            }

            is PlanningEvent.RestoreLocation -> {
                viewModelScope.launch {
                    saveLocation(recentlyDeletedLocation ?: return@launch)
                    getLocations()
                    fetchFromAPI(recentlyDeletedLocation ?: return@launch)
                    recentlyDeletedLocation = null
                }
            }

            is PlanningEvent.SearchInputTypeChanged -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        searchInputType = event.searchInputType
                    )
                }
            }

            PlanningEvent.ToggleSearchInputTypeSelection -> {
                _state.value = state.value.copy(
                    isSearchInputTypeSelectionSectionVisible = !state.value
                        .isSearchInputTypeSelectionSectionVisible
                )
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

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    private fun fetchFromAPI(location: Location) {
        viewModelScope.launch {
            if (!isOnline()) {
                uiEventChannel.send(UiEvent.LoadingError(R.string.connection_error))
                return@launch
            }
            planingUseCases.fetchWeatherAtLocationUseCase.invoke(
                location,
                _state.value.settings.weatherUnits
            ).collect { result ->
                when (result) {
                    is APIFetchResult.Success -> {

                    }

                    is APIFetchResult.Error -> {
                        when (result.apiErrorResult) {
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
                            uiEventChannel.send(UiEvent.LoadingError(R.string.dao_request_error))
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

    private fun getSettings() {
        viewModelScope.launch {
            planingUseCases.getSettingsUseCase.invoke().collect { settings ->
                _state.value = state.value.copy(
                    settings = settings
                )
            }
        }
    }
}
