package com.example.planyourjourney.feature_planing.presentation.weather.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.ui.theme.CloudBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    locationWeather: LocationWeather,
    weatherUnits: WeatherUnits = WeatherUnits(),
    color: Color = MaterialTheme.colorScheme.tertiary,
    cornerRadius: Dp = 10.dp,
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

    val pagerState = rememberPagerState { daysWithWeather.size }
    Box(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .padding(8.dp)
        ) {
            drawRoundRect(
                color = color,
                size = size,
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
        Column {
            HorizontalPager(
                pageSize = PageSize.Fill,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                state = pagerState
            ) { index ->
                val currentDayWeather = daysWithWeather[index]
                val timeOfDayWeather = currentDayWeather.filter {
                    it.time.hour == 8 || it.time.hour == 12 || it.time.hour == 16 || it.time.hour == 20
                }
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = if (currentDayWeather.first().time.toLocalDate() == LocalDate.now())
                            stringResource(id = R.string.today)
                        else
                            currentDayWeather.first().time
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = currentDayWeather.first().time.dayOfWeek.getDisplayName(TextStyle.FULL, locale).toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        timeOfDayWeather.forEach { weather ->
                            var weatherIconResourceInt = R.drawable.sunny_weather_icon
                            when (weather.cloudCover) {
                                in 0..25 -> {
                                    weatherIconResourceInt = R.drawable.sunny_weather_icon
                                }

                                in 26..50 -> {
                                    weatherIconResourceInt =
                                        R.drawable.cloudy_sun_25_50_weather_icon
                                }

                                in 51..75 -> {
                                    weatherIconResourceInt =
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
                            Column(modifier = Modifier.fillMaxHeight()) {
                                Row {
//                                Image(
//                                    modifier = Modifier.size(20.dp),
//                                    painter = painterResource(id = R.drawable.cloudy_100_weather_icon),
//                                    contentDescription = null,
//                                )
                                    Text(
                                        text = "${weather.time.hour}:00",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }

                                Image(
                                    painter = painterResource(id = weatherIconResourceInt),
                                    contentDescription = null,
//                                modifier = Modifier.fillMaxSize()
                                )
                                Row {
                                Image(
                                    modifier = Modifier.size(28.dp),
                                    painter = painterResource(id = R.drawable.thermometer_icon),
                                    contentDescription = null,
                                )
                                    Text(
                                        text = String.format("%.1f",weather.temperature2m) +
                                                weatherUnits.temperatureUnits.displayUnits,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                Row {
                                Image(
                                    modifier = Modifier.size(28.dp),
                                    painter = painterResource(id = R.drawable.drop_precipitation_rain_icon),
                                    contentDescription = null,
                                )
                                    Text(
                                        text = "${weather.precipitationProbability}" +
                                                weatherUnits.percentageUnits.displayUnits,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                                Row {
                                    Image(
                                        modifier = Modifier.size(28.dp),
                                        painter = painterResource(id = R.drawable.cloudy_100_weather_icon),
                                        contentDescription = null,
                                    )
                                    Text(
                                        text = "${weather.cloudCover}" +
                                                weatherUnits.percentageUnits.displayUnits,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            //this is the horizontal scroll list with days
//            LazyRow(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
//            ) {
//                items(count = daysWithWeather.size) { i ->
//                    val weather = daysWithWeather[i]
//                    Button(
//                        modifier = Modifier.wrapContentHeight(),
//                        colors = ButtonDefaults
//                            .buttonColors(
//                                containerColor = MaterialTheme.colorScheme.primary,
//                                contentColor = MaterialTheme.colorScheme.onPrimary
//                            ),
//                        shape = RoundedCornerShape(50),
//                        onClick = {
//                            scope.launch {
//                                pagerState.animateScrollToPage(daysWithWeather.indexOf(weather))
//                            }
//                        }
//                    ) {
//                        Text(
//                            text = "${weather.first().time.dayOfWeek.getDisplayName(TextStyle.FULL, locale)}\n" +
//                                    weather.first().time
//                                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
//                            maxLines = 2
//                        )
//                    }
//                }
//            }
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}