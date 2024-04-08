package com.example.planyourjourney.feature_planing.domain.util

enum class TemperatureUnits(val displayUnits: String, val queryUnits: String){
    CELSIUS("°C", "celsius"),
    FAHRENHEIT("°F", "fahrenheit")
}