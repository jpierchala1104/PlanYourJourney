package com.example.planyourjourney.feature_planing.domain.util

enum class WindSpeedUnits(val displayUnits: String, val queryUnits: String){
    KILOMETERS_PER_HOUR("km/h", "kmh"),
    METERS_PER_SECOND("m/s", "ms"),
    MILES_PER_HOUR("mph", "mph"),
    KNOTS("kn", "kn")
}