package com.example.planyourjourney.feature_planing.presentation.util

sealed class Screen(val route: String){
    object PlaningScreen: Screen("planing_screen")
}
