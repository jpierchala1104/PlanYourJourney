package com.example.planyourjourney.feature_planing.presentation.planning

import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Location

sealed class PlaningEvent {
    data object AddLocation : PlaningEvent()
    data class LocationNameChanged(val newLocationNameText: String) : PlaningEvent()
    data class CoordinatesChanged(val newCoordinates: Coordinates) : PlaningEvent()
    data class DeleteLocation(val location: Location) : PlaningEvent()
    data object RestoreLocation : PlaningEvent()
}
