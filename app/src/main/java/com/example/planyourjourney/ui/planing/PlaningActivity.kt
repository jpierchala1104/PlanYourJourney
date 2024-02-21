package com.example.planyourjourney.ui.planing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.planyourjourney.services.DecimalFormatter
import com.example.planyourjourney.ui.theme.PlanYourJourneyTheme

class PlaningActivity : ComponentActivity() {
    private val viewModel: PlaningViewModel by viewModels()
    private val decimalFormatter = DecimalFormatter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlanYourJourneyTheme{
                PlaningScreen(
                    viewModel,
                    decimalFormatter)
            }
        }
    }
}