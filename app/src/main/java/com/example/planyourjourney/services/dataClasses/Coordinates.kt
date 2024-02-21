package com.example.planyourjourney.services.dataClasses

import com.google.android.gms.maps.model.LatLng

data class Coordinates(
    var latitude: Double,
    var longitude: Double
) {
    fun toLatLng():LatLng{
        return LatLng(latitude, longitude)
    }
}
