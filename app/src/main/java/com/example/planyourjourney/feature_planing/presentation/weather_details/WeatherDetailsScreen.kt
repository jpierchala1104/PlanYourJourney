package com.example.planyourjourney.feature_planing.presentation.weather_details

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.model.WeatherVariables
import com.example.planyourjourney.feature_planing.domain.util.Language
import com.example.planyourjourney.feature_planing.presentation.destinations.SettingsScreenDestination
import com.example.planyourjourney.feature_planing.presentation.util.ChartService
import com.example.planyourjourney.feature_planing.presentation.weather_details.components.ChartSection
import com.example.planyourjourney.feature_planing.presentation.weather_details.components.WeatherVariablesMenu
import com.example.planyourjourney.feature_planing.presentation.weather_details.components.WeatherVariablesSelectionSection
import com.example.planyourjourney.ui.theme.PlanYourJourneyTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDateTime
import java.util.Locale
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun WeatherDetailsScreen(
    locationId: Int,
    navigator: DestinationsNavigator,
    viewModel: WeatherDetailsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppToolbar(
                modifier = Modifier.wrapContentHeight(),
                title = stringResource(R.string.app_name)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            navigator.popBackStack()
                        }
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    //imageVector = ImageVector.vectorResource(id = R.drawable.cloudy_100_weather_icon),
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
                .padding(innerPadding)
        ) {
            WeatherVariablesMenu(
                isWeatherVariablesSectionVisible = state.isWeatherVariablesSectionVisible,
                weatherVariables = state.settings.weatherVariables,
                onToggleWeatherVariablesSection = {
                    viewModel.onEvent(WeatherDetailsEvent.ToggleWeatherVariablesSection)
                },
                onWeatherVariablesChanged = {
                    viewModel.onEvent(WeatherDetailsEvent.WeatherVariablesChanged(it))
                }
            )
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 8.dp,0.dp,0.dp,0.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                Text(
//                    text = stringResource(id = R.string.pick_weather_variables),
//                    style = MaterialTheme.typography.bodyLarge
//                )
//                IconButton(
//                    onClick = {
//                        viewModel.onEvent(WeatherDetailsEvent.ToggleWeatherVariablesSection)
//                    }
//                ) {
//                    if (state.isWeatherVariablesSectionVisible)
//                    {
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowUp,
//                            contentDescription = null
//                        )
//                    }
//                    else {
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowDown,
//                            contentDescription = null
//                        )
//                    }
//                }
//            }
//            AnimatedVisibility(
//                visible = state.isWeatherVariablesSectionVisible,
//                enter = fadeIn() + slideInVertically(),
//                exit = fadeOut() + slideOutVertically()
//            ) {
//                WeatherVariablesSelectionSection(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                    weatherVariables = state.settings.weatherVariables,
//                    onWeatherVariablesChange = {
//                        viewModel.onEvent(WeatherDetailsEvent.WeatherVariablesChanged(it))
//                    }
//                )
//            }
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .size(8.dp)
            )
            ChartSection(
                modifier = Modifier,
                chartStateList = state.chartStateList
            )
        }
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun WeatherDetailsPreview() {
    val settings = Settings(
        language = Language.Polski,
        weatherUnits = WeatherUnits(),
        weatherVariables = WeatherVariables(
            isTemperature2mChecked = true,
            isPrecipitationProbabilityChecked = true,
            isRainChecked = false,
            isSnowfallChecked = false,
            isCloudCoverChecked = false,
            isRelativeHumidity2mChecked = false,
            isWindSpeed10mChecked = false
        )
    )
    val chartService = ChartService()
    chartService.setLocale(Locale(settings.language.localeCode))
    val today = LocalDateTime.now()
    var listOfHourlyWeatherData = listOf<HourlyWeather>()
    for (i: Int in 1..7) {
        for (j: Int in 0 until 24) {
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
    var chartStateList: List<ChartState> = listOf()
    if (settings.weatherVariables.isTemperature2mChecked) {
        chartStateList = chartStateList.plus(
            chartService.getLineChartState(
                chartTitleResourceId = R.string.temperature,
                chartValuesList = listOfHourlyWeatherData.map { it.temperature2m },
                localDateTimeList = listOfHourlyWeatherData.map { it.time },
                unit = settings.weatherUnits.temperatureUnits.displayUnits
            )
        )
    }
    if (settings.weatherVariables.isRelativeHumidity2mChecked) {
        chartStateList = chartStateList.plus(
            chartService.getLineChartState(
                chartTitleResourceId = R.string.relative_humidity,
                chartValuesList = listOfHourlyWeatherData.map { it.relativeHumidity2m.toDouble() },
                localDateTimeList = listOfHourlyWeatherData.map { it.time },
                unit = settings.weatherUnits.percentageUnits.displayUnits
            )
        )
    }
    if (settings.weatherVariables.isPrecipitationProbabilityChecked) {
        chartStateList = chartStateList.plus(
            chartService.getLineChartState(
                chartTitleResourceId = R.string.precipitation_probability,
                chartValuesList = listOfHourlyWeatherData.map { it.precipitationProbability.toDouble() },
                localDateTimeList = listOfHourlyWeatherData.map { it.time },
                unit = settings.weatherUnits.precipitationUnits.displayUnits
            )
        )
    }
    if (settings.weatherVariables.isRainChecked) {
        chartStateList = chartStateList.plus(
            chartService.getColumnChartState(
                chartTitleResourceId = R.string.rain,
                chartValuesList = listOfHourlyWeatherData.map { it.rain },
                localDateTimeList = listOfHourlyWeatherData.map { it.time },
                unit = settings.weatherUnits.precipitationUnits.displayUnits
            )
        )
    }
    if (settings.weatherVariables.isSnowfallChecked) {
        chartStateList = chartStateList.plus(
            chartService.getColumnChartState(
                chartTitleResourceId = R.string.snowfall,
                chartValuesList = listOfHourlyWeatherData.map { it.snowfall },
                localDateTimeList = listOfHourlyWeatherData.map { it.time },
                unit = settings.weatherUnits.precipitationUnits.displayUnits
            )
        )
    }
    if (settings.weatherVariables.isCloudCoverChecked) {
        chartStateList = chartStateList.plus(
            chartService.getLineChartState(
                chartTitleResourceId = R.string.cloud_cover,
                chartValuesList = listOfHourlyWeatherData.map { it.cloudCover.toDouble() },
                localDateTimeList = listOfHourlyWeatherData.map { it.time },
                unit = settings.weatherUnits.percentageUnits.displayUnits
            )
        )
    }
    if (settings.weatherVariables.isWindSpeed10mChecked) {
        chartStateList = chartStateList.plus(
            chartService.getLineChartState(
                chartTitleResourceId = R.string.wind_speed_at_10_m,
                chartValuesList = listOfHourlyWeatherData.map { it.windSpeed10m },
                localDateTimeList = listOfHourlyWeatherData.map { it.time },
                unit = settings.weatherUnits.windSpeedUnits.displayUnits
            )
        )
    }
    val state = WeatherDetailsState(
        isWeatherVariablesSectionVisible = false,
        locationWeather = LocationWeather(
            location = Location(
                locationName = "Warszawa",
                coordinates = Coordinates(0.0, 0.0),
                locationId = null
            ),
            hourlyWeatherList = listOfHourlyWeatherData
        ),
        settings = settings,
        isWeatherLoaded = true,
        isLoading = false,
        chartStateList = chartStateList
    )
    PlanYourJourneyTheme {
        Scaffold(
            topBar = {
                AppToolbar(
                    modifier = Modifier.wrapContentHeight(),
                    title = stringResource(R.string.app_name)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {

                            }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        //imageVector = ImageVector.vectorResource(id = R.drawable.cloudy_100_weather_icon),
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
                    .padding(innerPadding)
            ) {
                WeatherVariablesMenu(
                    isWeatherVariablesSectionVisible = state.isWeatherVariablesSectionVisible,
                    weatherVariables = state.settings.weatherVariables,
                    onToggleWeatherVariablesSection = {

                    },
                    onWeatherVariablesChanged = {

                    }
                )
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .size(8.dp)
                )
                ChartSection(
                    modifier = Modifier,
                    chartStateList = state.chartStateList
                )
            }
        }
    }
}