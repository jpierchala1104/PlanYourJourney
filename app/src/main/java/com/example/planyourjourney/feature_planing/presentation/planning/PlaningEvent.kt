package com.example.planyourjourney.feature_planing.presentation.planning

import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType

sealed class PlaningEvent {
    data object AddLocation : PlaningEvent()
    data class LocationNameChanged(val newLocationNameText: String) : PlaningEvent()
    data class CoordinatesChanged(val newCoordinates: Coordinates) : PlaningEvent()
    data class DeleteLocation(val location: Location) : PlaningEvent()
    data object RestoreLocation : PlaningEvent()
    data class SearchInputTypeChanged(val searchInputType: SearchInputType) : PlaningEvent()
    data object ToggleSearchInputTypeSelection : PlaningEvent()
}
