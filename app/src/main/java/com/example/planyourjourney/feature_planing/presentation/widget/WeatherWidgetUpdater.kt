package com.example.planyourjourney.feature_planing.presentation.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object WeatherWidgetUpdater {
    fun updateWeatherWidget(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val glanceId = GlanceAppWidgetManager(context).getGlanceIds(WeatherWidget::class.java)
                .firstOrNull()
            glanceId?.let {
                WeatherWidget().update(context, glanceId)
            }
        }
    }
}
