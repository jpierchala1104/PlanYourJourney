package com.example.planyourjourney.feature_planing.presentation.planning

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planyourjourney.R
import com.example.planyourjourney.core.presentation.AppToolbar
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.presentation.destinations.SettingsScreenDestination
import com.example.planyourjourney.feature_planing.presentation.destinations.WeatherDetailsScreenDestination
import com.example.planyourjourney.feature_planing.presentation.destinations.WeatherScreenDestination
import com.example.planyourjourney.feature_planing.presentation.planning.components.AddLocationSection
import com.example.planyourjourney.feature_planing.presentation.planning.components.CoordinatesInputSection
import com.example.planyourjourney.feature_planing.presentation.planning.components.LocationItem
import com.example.planyourjourney.feature_planing.presentation.planning.components.LocationList
import com.example.planyourjourney.feature_planing.presentation.planning.components.LocationNameInputSection
import com.example.planyourjourney.feature_planing.presentation.planning.components.SearchTypeSelectionMenu
import com.example.planyourjourney.feature_planing.presentation.planning.components.SearchTypeSelectionSection
import com.example.planyourjourney.feature_planing.presentation.util.DecimalFormatter
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType
import com.example.planyourjourney.feature_planing.presentation.util.UiEvent
import com.example.planyourjourney.ui.theme.PlanYourJourneyTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Composable
@Destination(start = true)
fun PlaningScreen(
    navigator: DestinationsNavigator,
    viewModel: PlaningViewModel = hiltViewModel()
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
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            navigator.navigate(
                                WeatherScreenDestination()
                            )
                        }
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.Filled.Settings,
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
            // TODO: check if this looks the same
            SearchTypeSelectionMenu(
                onToggleSearchInputTypeSelection = {
                    viewModel.onEvent(PlaningEvent.ToggleSearchInputTypeSelection)
                },
                isSearchInputTypeSelectionSectionVisible = state.isSearchInputTypeSelectionSectionVisible,
                searchInputType = state.searchInputType,
                onSearchInputTypeChange = {
                    viewModel.onEvent(PlaningEvent.SearchInputTypeChanged(it))
                }
            )
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
//                    .background(MaterialTheme.colorScheme.background),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = stringResource(id = R.string.search_input_type),
//                    style = MaterialTheme.typography.headlineMedium,
//                    color = MaterialTheme.colorScheme.onBackground
//                )
//                IconButton(
//                    onClick = {
//                        viewModel.onEvent(PlaningEvent.ToggleSearchInputTypeSelection)
//                    }
//                ) {
//                    if (state.isSearchInputTypeSelectionSectionVisible)
//                    {
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowUp,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.onBackground
//                        )
//                    }
//                    else {
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowDown,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.onBackground
//                        )
//                    }
//                }
//            }
//            AnimatedVisibility(
//                visible = state.isSearchInputTypeSelectionSectionVisible,
//                enter = fadeIn() + slideInVertically(),
//                exit = fadeOut() + slideOutVertically()
//            ) {
//                SearchTypeSelectionSection(
//                    searchInputType = state.searchInputType,
//                    onSearchInputTypeChange = {
//                        viewModel.onEvent(PlaningEvent.SearchInputTypeChanged(it))
//                    }
//                )
//            }
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .size(8.dp)
            )
            // TODO: check if this looks the same
            AddLocationSection(
                modifier = Modifier.fillMaxWidth(),
                searchInputType = state.searchInputType,
                weatherCoordinates = weatherCoordinates,
                weatherLocationName = weatherLocationName,
                onCoordinatesChanged = {
                    viewModel.onEvent(PlaningEvent.CoordinatesChanged(it))
                },
                onLocationNameChanged = {
                    viewModel.onEvent(PlaningEvent.LocationNameChanged(it))
                },
                onAddLocation = {
                    viewModel.onEvent(PlaningEvent.AddLocation)
                },
                decimalFormatter = decimalFormatter
            )
//            Column(
//                Modifier.padding(8.dp)
//            ) {
//                when (state.searchInputType) {
//                    SearchInputType.LatitudeAndLongitude -> CoordinatesInputSection(
//                        coordinates = weatherCoordinates,
//                        onValueChange = {
//                            viewModel.onEvent(PlaningEvent.CoordinatesChanged(it))
//                        },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                        decimalFormatter = decimalFormatter
//                    )
//
//                    SearchInputType.LocationName -> LocationNameInputSection(
//                        locationName = weatherLocationName,
//                        onValueChange = {
//                            viewModel.onEvent(PlaningEvent.LocationNameChanged(it))
//                        }
//                    )
//                    //SearchInputType.Map -> Map(viewModel, Modifier.fillMaxSize())
//                }
//
//                Button(onClick = {
//                    viewModel.onEvent(PlaningEvent.AddLocation)
//                }) {
//                    Text(text = stringResource(id = R.string.add_location))
//                }
//            }
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .size(8.dp)
            )
            // TODO: check if this looks the same
            LocationList(
                isLoading = state.isLoading,
                isLocationLoaded = state.isLocationLoaded,
                locationList = state.locationList,
                onDeleteLocation = {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = context.getString(R.string.location_deleted),
                            actionLabel = context.getString(R.string.undo),
                            duration = SnackbarDuration.Long
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(PlaningEvent.RestoreLocation)
                        }
                    }
                },
                navigator = navigator
            )
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                if (state.isLoading) {
//                    CircularProgressIndicator(
//                        color = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier
//                            .wrapContentSize()
//                            .size(56.dp),
//                        strokeWidth = 6.dp,
//                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
//                    )
//                } else {
//                    if (!state.isLocationLoaded || state.locationList.isEmpty()) {
//                        Text(
//                            text = stringResource(id = R.string.locations_empty)
//                        )
//                    } else {
//                        LazyColumn(
//                            modifier = Modifier.fillMaxSize()
//                        ) {
//                            items(count = state.locationList.size) { i ->
//                                val location = state.locationList[i]
//                                LocationItem(
//                                    location = location,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .clickable {
//                                            navigator.navigate(
//                                                WeatherDetailsScreenDestination(location.locationId!!)
//                                            )
//                                        },
//                                    //.padding(16.dp)
//                                    onDeleteClick = {
//                                        // TODO: Maybe change this to confirm delete scaffoldState
//                                        //  with no restore option
//                                        viewModel.onEvent(PlaningEvent.DeleteLocation(location))
//                                        scope.launch {
//                                            val result = snackbarHostState.showSnackbar(
//                                                message = context.getString(R.string.location_deleted),
//                                                actionLabel = context.getString(R.string.undo),
//                                                duration = SnackbarDuration.Long
//                                            )
//                                            if (result == SnackbarResult.ActionPerformed) {
//                                                viewModel.onEvent(PlaningEvent.RestoreLocation)
//                                            }
//                                        }
//                                    }
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
                SearchTypeSelectionMenu(
                    onToggleSearchInputTypeSelection = {

                    },
                    isSearchInputTypeSelectionSectionVisible = state.isSearchInputTypeSelectionSectionVisible,
                    searchInputType = state.searchInputType,
                    onSearchInputTypeChange = {

                    }
                )
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .size(8.dp)
                )
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
                    isLoading = state.isLoading,
                    isLocationLoaded = state.isLocationLoaded,
                    locationList = state.locationList,
                    onDeleteLocation = {

                    },
                    navigator = null
                )
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