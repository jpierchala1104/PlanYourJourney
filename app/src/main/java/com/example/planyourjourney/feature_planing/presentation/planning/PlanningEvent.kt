package com.example.planyourjourney.feature_planing.presentation.planning

import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType

sealed class PlanningEvent {
    data object AddLocation : PlanningEvent()
    data class LocationNameChanged(val newLocationNameText: String) : PlanningEvent()
    data class CoordinatesChanged(val newCoordinates: Coordinates) : PlanningEvent()
    data class DeleteLocation(val location: Location) : PlanningEvent()
    data object RestoreLocation : PlanningEvent()
    data class SearchInputTypeChanged(val searchInputType: SearchInputType) : PlanningEvent()
    data object ToggleSearchInputTypeSelection : PlanningEvent()
}
