package com.example.planyourjourney.feature_planing.presentation.widget

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import com.example.planyourjourney.di.WidgetEntryPoint
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.use_case.WeatherWidgetUseCases
import dagger.hilt.android.EntryPointAccessors

class WeatherWidget : GlanceAppWidget() {
    private fun getWeatherWidgetUseCases(context: Context): WeatherWidgetUseCases {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )
        return entryPoint.weatherWidgetUseCases()
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        try {
            val weatherWidgetUseCases = getWeatherWidgetUseCases(context)
            val widgetSettings = weatherWidgetUseCases.getSettingsForWidgetUseCase.invoke()

            var widgetWeather: LocationWeather? = null

            if (widgetSettings.widgetLocation != null) {
                if (widgetSettings.widgetLocation.locationId != null) {
                    widgetWeather = weatherWidgetUseCases.getPreloadedWidgetDataUseCase.invoke()
                    WeatherWidget().updateAll(context)
                }
            }
            // TODO: edit the widget, change the day (and maybe closest time because you don't need
            // TODO: the weather at 8 am if its already 1 pm 
            // TODO: clicking on the widget should open the app 
            // TODO: the update widget doesn't work 
            provideContent {
                GlanceTheme {
                    WeatherWidgetScreen(
                        context = context,
                        settings = widgetSettings,
                        weather = widgetWeather
                    )
                }
            }
        } catch (ex: Exception) {
            Log.e("Widget", "msg: $ex")
        }
    }
}