package com.example.planyourjourney.feature_planing.presentation.widget.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceComposable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.size
import androidx.glance.text.Text
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.util.toKotlinLocalDate
import com.example.planyourjourney.feature_planing.domain.util.toLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@Composable
@GlanceComposable
fun WeatherWidgetCard(
    context: Context,
    modifier: GlanceModifier = GlanceModifier,
    locationWeather: LocationWeather,
    weatherUnits: WeatherUnits = WeatherUnits(),
    locale: Locale
) {
    val indexes = locationWeather.hourlyWeatherList.withIndex().filter { hourlyWeather ->
        hourlyWeather.value.time.hour == 0
    }.map { it.index }
    var daysWithWeather: List<List<HourlyWeather>> = listOf()
    indexes.forEach { i ->
        daysWithWeather =
            daysWithWeather.plus(listOf(locationWeather.hourlyWeatherList.subList(i, i + 23)))
    }

    Box(
        modifier = modifier
    ) {
        val currentDayWeather = daysWithWeather[0]
        val timeOfDayWeather = currentDayWeather.filter {
            it.time.hour == 8 || it.time.hour == 12 || it.time.hour == 16 || it.time.hour == 20
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (currentDayWeather.first().time.toLocalDate() == java.time.LocalDate.now().toKotlinLocalDate())
                    context.getString(R.string.today)
                else
                    currentDayWeather.first().time.toJavaLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            )
            Text(
                text = currentDayWeather.first().time.dayOfWeek.getDisplayName(
                    TextStyle.FULL,
                    locale
                ).toString()
            )
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                timeOfDayWeather.forEach { weather ->
                    var weatherIconResourceInt = R.drawable.sunny_weather_icon
                    when (weather.cloudCover) {
                        in 0..25 -> {
                            weatherIconResourceInt = if (weather.time.hour > 17)
                                R.drawable.moon_weather_icon
                            else
                                R.drawable.sunny_weather_icon
                        }

                        in 26..50 -> {
                            weatherIconResourceInt = if (weather.time.hour > 17)
                                R.drawable.moon_cloudy_weather_icon
                            else
                                R.drawable.cloudy_sun_25_50_weather_icon

                        }

                        in 51..75 -> {
                            weatherIconResourceInt = if (weather.time.hour > 17)
                                R.drawable.moon_cloudy_weather_icon
                            else
                                R.drawable.cloudy_sun_50_75_weather_icon

                        }

                        in 76..100 -> {
                            weatherIconResourceInt = R.drawable.cloudy_100_weather_icon
                        }
                    }
                    if (weather.precipitationProbability >= 40) {
                        weatherIconResourceInt = R.drawable.cloudy_rainy_weather_icon
                    }
                    // TODO: need styling
                    Column(modifier = GlanceModifier.fillMaxHeight()) {
                        Row {
                            Text(
                                text = "${weather.time.hour}:00"
                            )
                        }

                        Image(
                            provider = ImageProvider(resId = weatherIconResourceInt),
                            contentDescription = "weather Icon",
//                                modifier = Modifier.fillMaxSize()
                        )
                        Row {
                            Image(
                                modifier = GlanceModifier.size(28.dp),
                                provider = ImageProvider(resId = R.drawable.thermometer_icon),
                                contentDescription = "thermometer icon",
                            )
                            Text(
                                text = String.format("%.1f", weather.temperature2m) +
                                        weatherUnits.temperatureUnits.displayUnits
                            )
                        }
                        Row {
                            Image(
                                modifier = GlanceModifier.size(28.dp),
                                provider = ImageProvider(resId = R.drawable.drop_precipitation_rain_icon),
                                contentDescription = "rain icon",
                            )
                            Text(
                                text = "${weather.precipitationProbability}" +
                                        weatherUnits.percentageUnits.displayUnits
                            )
                        }
                        Row {
                            Image(
                                modifier = GlanceModifier.size(28.dp),
                                provider = ImageProvider(resId = R.drawable.cloudy_100_weather_icon),
                                contentDescription = "cloud icon",
                            )
                            Text(
                                text = "${weather.cloudCover}" +
                                        weatherUnits.percentageUnits.displayUnits
                            )
                        }
                    }
                }
            }
        }
    }
}