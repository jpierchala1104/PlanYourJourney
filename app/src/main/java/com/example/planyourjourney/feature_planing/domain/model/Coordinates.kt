package com.example.planyourjourney.feature_planing.domain.model

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    var latitude: Double,
    var longitude: Double
) {
    fun toLatLng():LatLng{
        return LatLng(latitude, longitude)
    }
}
