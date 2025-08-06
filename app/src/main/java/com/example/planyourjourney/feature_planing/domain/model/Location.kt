package com.example.planyourjourney.feature_planing.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val locationId: Int? = null,
    val locationName: String? = null,
    val coordinates: Coordinates = Coordinates(0.0,0.0)
)
