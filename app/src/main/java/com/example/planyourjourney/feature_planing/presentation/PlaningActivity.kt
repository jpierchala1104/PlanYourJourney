package com.example.planyourjourney.feature_planing.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planyourjourney.feature_planing.presentation.planning.PlaningViewModel
import com.example.planyourjourney.feature_planing.presentation.planning.PlaningScreen
import com.example.planyourjourney.feature_planing.presentation.util.Screen
import com.example.planyourjourney.ui.theme.PlanYourJourneyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaningActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlanYourJourneyTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.PlaningScreen.route
                    ){
                        composable(route = Screen.PlaningScreen.route) {
                            PlaningScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}