package com.example.planyourjourney.feature_planing.presentation.planning

import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.presentation.util.Language
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType

data class PlanningState(
    val locationList: List<Location> = listOf(),
    val searchInputType: SearchInputType = SearchInputType.LocationName,
    val isLoading: Boolean = false,
    val isLocationLoaded: Boolean = false,
    val isCoordinatesValueOrLocationNameChanged: Boolean = false,
    val settings: Settings = Settings(),
    val isSearchInputTypeSelectionSectionVisible: Boolean = false
)