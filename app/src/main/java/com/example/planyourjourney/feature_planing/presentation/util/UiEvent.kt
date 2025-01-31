package com.example.planyourjourney.feature_planing.presentation.util

sealed class UiEvent {
    data object LocationsLoaded : UiEvent()
    data class LoadingError(val messageResourceId: Int) : UiEvent()
    data class ConnectionError(val messageResourceId: Int) : UiEvent()
}