package com.example.planyourjourney.feature_planing.presentation.planning

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planyourjourney.R
import com.example.planyourjourney.core.presentation.AppToolbar
import com.example.planyourjourney.core.presentation.BottomNavigationMenu
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.presentation.destinations.SettingsScreenDestination
import com.example.planyourjourney.feature_planing.presentation.destinations.WeatherScreenDestination
import com.example.planyourjourney.feature_planing.presentation.planning.components.AddLocationSection
import com.example.planyourjourney.feature_planing.presentation.planning.components.LocationList
import com.example.planyourjourney.feature_planing.presentation.util.DecimalFormatter
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType
import com.example.planyourjourney.feature_planing.presentation.util.UiEvent
import com.example.planyourjourney.ui.theme.PlanYourJourneyTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Composable
@Destination
fun PlanningScreen(
    navigator: DestinationsNavigator,
    viewModel: PlanningViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val weatherCoordinates = viewModel.weatherCoordinates.value
    val weatherLocationName = viewModel.weatherLocationName.value
    val scope = rememberCoroutineScope()
    val decimalFormatter = DecimalFormatter()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    //LocalConfiguration.current.setLocale(Locale(state.settings.language.localeCode))

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = {
            AppToolbar(
                modifier = Modifier.wrapContentHeight(),
                title = stringResource(R.string.app_name)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            navigator.navigate(
                                direction = SettingsScreenDestination(),
                                builder = {
                                    popUpTo(route = SettingsScreenDestination.route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
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

                        is UiEvent.ConnectionError -> {
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
            // TODO: doesn't look great, for now its commented
//            SearchTypeSelectionMenu(
//                onToggleSearchInputTypeSelection = {
//                    viewModel.onEvent(PlaningEvent.ToggleSearchInputTypeSelection)
//                },
//                isSearchInputTypeSelectionSectionVisible = state.isSearchInputTypeSelectionSectionVisible,
//                searchInputType = state.searchInputType,
//                onSearchInputTypeChange = {
//                    viewModel.onEvent(PlaningEvent.SearchInputTypeChanged(it))
//                }
//            )
//
//            HorizontalDivider(
//                Modifier
//                    .fillMaxWidth()
//                    .size(8.dp)
//            )

            AddLocationSection(
                modifier = Modifier.fillMaxWidth(),
                searchInputType = state.searchInputType,
                weatherCoordinates = weatherCoordinates,
                weatherLocationName = weatherLocationName,
                onCoordinatesChanged = {
                    viewModel.onEvent(PlanningEvent.CoordinatesChanged(it))
                },
                onLocationNameChanged = {
                    viewModel.onEvent(PlanningEvent.LocationNameChanged(it))
                },
                onAddLocation = {
                    viewModel.onEvent(PlanningEvent.AddLocation)
                },
                decimalFormatter = decimalFormatter
            )

            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .size(8.dp)
            )

            LocationList(
                modifier = Modifier.weight(1f),
                isLoading = state.isLoading,
                isLocationLoaded = state.isLocationLoaded,
                locationList = state.locationList,
                onDeleteLocation = {
                    viewModel.onEvent(PlanningEvent.DeleteLocation(it))
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = context.getString(R.string.location_deleted),
                            actionLabel = context.getString(R.string.undo),
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(PlanningEvent.RestoreLocation)
                        }
                    }
                },
                navigator = navigator
            )
            BottomNavigationMenu{
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .clickable {
                            //current screen
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(32.dp)
                    )
                    Text(
                        text = stringResource(R.string.add_locations),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .clickable {
                            navigator.navigate(
                                direction = WeatherScreenDestination(),
                                builder = {
                                    popUpTo(route = WeatherScreenDestination.route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sunny_weather_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(32.dp)

                    )
                    Text(
                        text = stringResource(R.string.weather),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(locale = "pl")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PlaningScreenPreview() {
    val state = PlanningState(
        locationList = listOf(
            Location(
                locationName = "Warszawa",
                coordinates = Coordinates(0.0, 0.0),
                locationId = null
            ),
            Location(
                locationName = "Cieszyn",
                coordinates = Coordinates(1.0, 2.0),
                locationId = null
            )
        ),
        searchInputType = SearchInputType.LocationName,
        isLoading = false,
        isLocationLoaded = true,
        settings = Settings(),
        isSearchInputTypeSelectionSectionVisible = false
    )

    val weatherCoordinates = Coordinates(0.0, 0.0)
    val weatherLocationName = ""
    val decimalFormatter = DecimalFormatter()
    PlanYourJourneyTheme {
        Scaffold(
            snackbarHost = {

            },
            topBar = {
                AppToolbar(
                    modifier = Modifier.wrapContentHeight(),
                    title = stringResource(R.string.app_name)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
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
//                SearchTypeSelectionMenu(
//                    onToggleSearchInputTypeSelection = {
//
//                    },
//                    isSearchInputTypeSelectionSectionVisible = state.isSearchInputTypeSelectionSectionVisible,
//                    searchInputType = state.searchInputType,
//                    onSearchInputTypeChange = {
//
//                    }
//                )
//                HorizontalDivider(
//                    Modifier
//                        .fillMaxWidth()
//                        .size(8.dp)
//                )
                AddLocationSection(
                    modifier = Modifier.fillMaxWidth(),
                    searchInputType = state.searchInputType,
                    weatherCoordinates = weatherCoordinates,
                    weatherLocationName = weatherLocationName,
                    onCoordinatesChanged = {

                    },
                    onLocationNameChanged = {

                    },
                    onAddLocation = {

                    },
                    decimalFormatter = decimalFormatter
                )
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .size(8.dp)
                )
                LocationList(
                    modifier = Modifier.weight(1f),
                    isLoading = state.isLoading,
                    isLocationLoaded = state.isLocationLoaded,
                    locationList = state.locationList,
                    onDeleteLocation = {

                    },
                    navigator = null
                )
                BottomNavigationMenu{
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {

                                }
                        )
                        Text(
                            text = stringResource(R.string.add_locations),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.sunny_weather_icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {

                                }
                        )
                        Text(
                            text = stringResource(R.string.weather),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}


//@Composable
//fun Map(
//    viewModel: PlaningViewModel,
//    modifier: Modifier = Modifier
//) {
//    val cameraPositionState = rememberCameraPositionState{
//        position = CameraPosition.fromLatLngZoom(viewModel.coordinates.toLatLng(), 10f)
//    }
//    GoogleMap(
//        modifier = modifier,
//        cameraPositionState = cameraPositionState
//    ){
//        Marker(
//            state = MarkerState(position = viewModel.coordinates.toLatLng()),
//            title = "Marker on Location",
//            snippet = "Marker on Location"
//        )
//    }
//}