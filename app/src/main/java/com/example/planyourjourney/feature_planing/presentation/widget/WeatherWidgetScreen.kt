package com.example.planyourjourney.feature_planing.presentation.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceComposable
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.presentation.widget.components.WeatherWidgetCard
import java.util.Locale

@Composable
@GlanceComposable
fun WeatherWidgetScreen(
    context: Context,
    settings: Settings,
    weather: LocationWeather?
) {
    // TODO: widget opens but its loading all the time
    // TODO: check composable fun's for errors (scaffold, box and the composable annotation)
    Scaffold {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (weather == null) {
                Text(text = context.getString(R.string.no_location_selected))
            } else {
                WeatherWidgetCard(
                    context,
                    locationWeather = weather,
                    weatherUnits = settings.weatherUnits,
                    locale = Locale(settings.language.localeCode)
                )
//                Spacer(modifier = GlanceModifier.width(8.dp))
//                Button(
//                    text = context.getString(R.string.refresh),
//                    onClick = actionRunCallback<RefreshWeatherWidgetAction>()
//                )
            }
        }
    }
}