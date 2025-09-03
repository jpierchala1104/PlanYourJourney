package com.example.planyourjourney.feature_planing.presentation.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object WeatherWidgetUpdater {
    fun updateWeatherWidget(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            WeatherWidget().updateAll(context)
        }
    }
}
