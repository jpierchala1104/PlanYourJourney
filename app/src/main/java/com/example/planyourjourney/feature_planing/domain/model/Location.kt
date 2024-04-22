package com.example.planyourjourney.feature_planing.domain.model

data class Location(
    val locationId: Int?,
    val locationName: String?,
    val coordinates: Coordinates,
    var isLoaded: Boolean = false // TODO: do i need this? it doesn't work right now anyway i think
)
