package com.example.planyourjourney.feature_planing.presentation.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.feature_planing.domain.use_case.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {
    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    init {
        getSettings()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SettingsSaved -> {
                // TODO: either call API if weather units changed, or make a unit converter/calculator
                settingsUseCases.saveSettingsUseCase
            }
        }
    }

    private fun getSettings() {
        viewModelScope.launch {
            settingsUseCases.getSettingsUseCase.invoke().collect { settings ->
                _state.value = state.value.copy(
                    settings = settings
                )
            }
        }
    }
}