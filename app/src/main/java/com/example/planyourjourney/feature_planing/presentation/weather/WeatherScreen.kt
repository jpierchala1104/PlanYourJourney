package com.example.planyourjourney.feature_planing.presentation.weather

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planyourjourney.R
import com.example.planyourjourney.core.presentation.AppToolbar
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.presentation.destinations.PlaningScreenDestination
import com.example.planyourjourney.feature_planing.presentation.destinations.SettingsScreenDestination
import com.example.planyourjourney.feature_planing.presentation.destinations.WeatherDetailsScreenDestination
import com.example.planyourjourney.feature_planing.presentation.util.UiEvent
import com.example.planyourjourney.feature_planing.presentation.weather.components.WeatherCard
import com.example.planyourjourney.feature_planing.presentation.weather.components.WeatherList
import com.example.planyourjourney.ui.theme.PlanYourJourneyTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun WeatherScreen(
    navigator: DestinationsNavigator,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current

    // TODO: add delete button to the weather card list item,
    //  make a refresh action? either a button so it doesn't try to refresh by accident?
    //  maybe a checker when was the weather last updated and set it to ... or something,


    Scaffold(
        topBar = {
            AppToolbar(
                modifier = Modifier.wrapContentHeight(),
                title = stringResource(R.string.app_name)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            navigator.navigate(
                                PlaningScreenDestination()
                            )
                        }
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    //painter = painterResource(id = R.drawable.cloudy_100_weather_icon) -> this is in Image,
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            navigator.navigate(
                                SettingsScreenDestination()
                            )
                        }
                )
            }
        }
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//
//                },
//                containerColor = Color.White
//            ) {
//                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
//            }
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LaunchedEffect(key1 = context) {
                viewModel.uiEvents.collect { event ->
                    when (event) {
                        is UiEvent.LoadingError -> {
                            Toast.makeText(
                                context,
                                context.getString(event.messageResourceId),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        UiEvent.LocationsLoaded -> {}
                    }
                }
            }
            // TODO: check if its the same
            WeatherList(
                locationWeatherList = state.locationWeatherList,
                isLoading = state.isLoading,
                onRefreshLocation = {
                    viewModel.onEvent(WeatherEvent.RefreshLocationWeather(it))
                },
                scope = scope,
                weatherUnits = state.settings.weatherUnits,
                localeCode = state.settings.language.localeCode,
                navigator = navigator
            )
//            Text(text = stringResource(R.string.weather_forecast))
//            HorizontalDivider(Modifier.size(8.dp))
//
//            if (state.isLoading) {
//                CircularProgressIndicator(
//                    color = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .size(72.dp),
//                    strokeWidth = 6.dp,
//                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
//                )
//            } else {
//                if (state.locationWeatherList.isEmpty()) {
//                    Text(text = stringResource(R.string.locations_empty))
//                } else {
//                    LazyColumn(modifier = Modifier.fillMaxSize()) {
//                        items(count = state.locationWeatherList.size) { i ->
//                            val location = state.locationWeatherList[i]
//                            Column(
//                                modifier = Modifier
//                                    .padding(8.dp)
//                                    .fillMaxSize()
//                                    .clickable {
//                                        navigator.navigate(
//                                            WeatherDetailsScreenDestination(location.location.locationId!!)
//                                        )
//                                    }
//                            ) {
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                )
//                                {
//                                    Column {
//                                        Text(
//                                            text = location.location.locationName.orEmpty(),
//                                            style = MaterialTheme.typography.headlineMedium,
//                                            color = MaterialTheme.colorScheme.onSurface,
//                                            maxLines = 1,
//                                            overflow = TextOverflow.Ellipsis
//                                        )
//                                        Column {
//                                            Text(
//                                                text = stringResource(id = R.string.latitude_colon) +
//                                                        location.location.coordinates.latitude.toString()
//                                            )
//                                            Text(
//                                                text = stringResource(id = R.string.longitude_colon) +
//                                                        location.location.coordinates.longitude.toString()
//                                            )
//                                        }
//                                    }
//                                    Icon(
//                                        imageVector = Icons.Default.Refresh,
//                                        contentDescription = null,
//                                        tint = MaterialTheme.colorScheme.onBackground,
//                                        modifier = Modifier
//                                            .size(32.dp)
//                                            .clickable {
//                                                viewModel.onEvent(
//                                                    WeatherEvent.RefreshLocationWeather(
//                                                        location.location
//                                                    )
//                                                )
//                                            }
//                                    )
//                                }
//                                WeatherCard(
//                                    scope = scope,
//                                    locationWeather = location,
//                                    weatherUnits = state.settings.weatherUnits,
//                                    locale = Locale(state.settings.language.localeCode)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
        }
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun WeatherScreenPreview() {
    val today = LocalDateTime.now()
    var listOfHourlyWeatherData = listOf<HourlyWeather>()
    for(i: Int in 1 .. 7){
        for (j: Int in 0 until 24){
            listOfHourlyWeatherData = listOfHourlyWeatherData.plus(
                HourlyWeather(
                    time = LocalDateTime.of(
                        today.year,
                        today.month,
                        i,
                        j,
                        0
                    ),
                    temperature2m = 15.0 + Random.nextDouble(-5.0, 5.0),
                    relativeHumidity2m = 5 + Random.nextInt(-2, 2),
                    precipitationProbability = 50 + Random.nextInt(-49, 49),
                    rain = 10.0 + Random.nextDouble(-9.0, 9.0),
                    snowfall = 0.0,
                    cloudCover = 50 + Random.nextInt(-49, 49),
                    windSpeed10m = 7.0 + Random.nextDouble(-6.0, 13.0)
                )
            )
        }
    }
    val state = WeatherState(
        locationWeatherList = listOf(
            LocationWeather(
                location = Location(
                    locationName = "Warszawa",
                    coordinates = Coordinates(0.0, 0.0),
                    locationId = null
                ),
                hourlyWeatherList = listOfHourlyWeatherData
            ),
            LocationWeather(
                location = Location(
                    locationName = "Cieszyn",
                    coordinates = Coordinates(1.0, 2.0),
                    locationId = null
                ),
                hourlyWeatherList = listOfHourlyWeatherData
            )
        ),
        isLoading = false,
        settings = Settings(),
        isWeatherLoaded = true
    )
    val scope = rememberCoroutineScope()
    PlanYourJourneyTheme {
        Scaffold(
            topBar = {
                AppToolbar(
                    modifier = Modifier.wrapContentHeight(),
                    title = stringResource(R.string.app_name)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {

                            }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        //painter = painterResource(id = R.drawable.cloudy_100_weather_icon) -> this is in Image,
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {

                            }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // TODO: check if its the same
                WeatherList(
                    locationWeatherList = state.locationWeatherList,
                    isLoading = state.isLoading,
                    onRefreshLocation = {

                    },
                    scope = scope,
                    weatherUnits = state.settings.weatherUnits,
                    localeCode = state.settings.language.localeCode,
                    navigator = null
                )
            }
        }
    }
}