package com.example.planyourjourney.feature_planing.presentation.planning

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planyourjourney.R
import com.example.planyourjourney.core.presentation.AppToolbar
import com.example.planyourjourney.feature_planing.presentation.destinations.WeatherDetailsScreenDestination
import com.example.planyourjourney.feature_planing.presentation.destinations.WeatherScreenDestination
import com.example.planyourjourney.feature_planing.presentation.planning.components.CoordinatesInputSection
import com.example.planyourjourney.feature_planing.presentation.planning.components.LocationItem
import com.example.planyourjourney.feature_planing.presentation.planning.components.LocationNameInputSection
import com.example.planyourjourney.feature_planing.presentation.util.DecimalFormatter
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType
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
                    tint = Color.White,
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
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
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
//            LaunchedEffect(key1 = context){
//                viewModel.uiEvents.collect{ event ->
//                    when(event){
//                        is PlaningViewModel.UiEvent.LoadingError -> {
//                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
//                        }
//                        PlaningViewModel.UiEvent.LocationsLoaded -> TODO()
//                    }
//                }
//            }
            Column(
                Modifier.padding(10.dp)
            ) {
                when (viewModel.state.value.settings.searchInputType) {
                    SearchInputType.LatitudeAndLongitude -> CoordinatesInputSection(
                        coordinates = weatherCoordinates,
                        onValueChange = {
                            viewModel.onEvent(PlaningEvent.CoordinatesChanged(it))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        decimalFormatter = decimalFormatter
                    )

                    SearchInputType.LocationName -> LocationNameInputSection(
                        locationName = weatherLocationName,
                        onValueChange = {
                            viewModel.onEvent(PlaningEvent.LocationNameChanged(it))
                        }
                    )
                    //SearchInputType.Map -> Map(viewModel, Modifier.fillMaxSize())
                }

                Button(onClick = {
                    viewModel.onEvent(PlaningEvent.AddLocation)
                }) {
                    Text(text = "Add Location")
                }
            }
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .size(20.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .wrapContentSize()
                            .size(56.dp),
                        strokeWidth = 6.dp,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                } else {
                    if (!state.isLocationLoaded || state.locationList.isEmpty()) {
                        Text(
                            text = "No location added"
                        )
                    } else {
                        // TODO: show location list
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(count = state.locationList.size) { i ->
                                val location = state.locationList[i]
                                LocationItem(
                                    location = location,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navigator.navigate(
                                                WeatherDetailsScreenDestination(location.locationId!!)
                                            )
                                        },
                                    //.padding(16.dp)
                                    onDeleteClick = {
                                        // TODO: Maybe change this to confirm delete scaffoldState
                                        //  with no restore option
                                        viewModel.onEvent(PlaningEvent.DeleteLocation(location))
                                        scope.launch {
                                            val result = snackbarHostState.showSnackbar(
                                                message = "Location deleted",
                                                actionLabel = "Undo",
                                                duration = SnackbarDuration.Long
                                            )
                                            if (result == SnackbarResult.ActionPerformed) {
                                                viewModel.onEvent(PlaningEvent.RestoreLocation)
                                            }
                                        }
                                    },
                                    isLoaded = location.isLoaded
                                )
                            }
                        }
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