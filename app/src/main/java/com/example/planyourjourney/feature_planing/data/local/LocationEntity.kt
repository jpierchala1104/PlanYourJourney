package com.example.planyourjourney.feature_planing.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(
    @PrimaryKey val locationId: Int? = null,
    val locationName: String? = null, // can add a function to get location from coordinates, but for now will be nullable if weather searched with coordinates
    val latitude: Double,
    val longitude: Double
)
