package com.example.planyourjourney.feature_planing.presentation.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.use_case.SettingsUseCases
import com.example.planyourjourney.feature_planing.domain.util.Resource
import com.example.planyourjourney.feature_planing.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {
    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    private var oldWeatherUnits: WeatherUnits = WeatherUnits()

    init {
        getSettings()
        getLocations()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
//            is SettingsEvent.SaveSettings -> {
//                viewModelScope.launch {
//
//                }
//            }

            is SettingsEvent.SettingsChanged -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        settings = event.settings
                    )
                    settingsUseCases.saveSettingsUseCase.invoke(_state.value.settings)
                    settingsUseCases.updateUnitsUseCase.invoke(_state.value.settings, oldWeatherUnits)
                    val appLocale = LocaleListCompat
                        .forLanguageTags(_state.value.settings.language.localeCode)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            }
        }
    }

    private fun getSettings() {
        viewModelScope.launch {
            settingsUseCases.getSettingsUseCase.invoke().collect { settings ->
                _state.value = state.value.copy(
                    settings = settings
                )
                oldWeatherUnits = settings.weatherUnits
            }
        }
    }

    private fun getLocations() {
        viewModelScope.launch {
            settingsUseCases.getLocationsUseCase.invoke()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { locations ->
                                _state.value = state.value.copy(
                                    locationList = locations
                                )
                            }
                            _state.value = state.value.copy(
                                isSettingsLoaded = true, isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                isSettingsLoaded = false, isLoading = false
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}