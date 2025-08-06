package com.example.planyourjourney.feature_planing.presentation.settings

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planyourjourney.R
import com.example.planyourjourney.core.presentation.AppToolbar
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.presentation.settings.components.SettingsSection
import com.example.planyourjourney.ui.theme.PlanYourJourneyTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun SettingsScreen(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            navigator.popBackStack()
                        }
                )
//                Spacer(modifier = Modifier.size(8.dp))
//                Icon(
//                    imageVector = Icons.Filled.Add,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onPrimary,
//                    modifier = Modifier
//                        .size(32.dp)
//                        .clickable {
//                            navigator.navigate(
//                                PlaningScreenDestination()
//                            )
//                        }
//                )
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
            SettingsSection(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                settings = state.settings,
                onSettingsChanged = {
                    viewModel.onEvent(SettingsEvent.SettingsChanged(it))
                },
                locationList = state.locationList
//                onSaveSettings = {
//                    viewModel.onEvent(SettingsEvent.SaveSettings)
//                },
//                navigator = navigator
            )
        }
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SettingsPreview() {
    val state = SettingsState(
        isLoading = false,
        isSettingsLoaded = true,
        settings = Settings()
    )
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {

                        }
                )
//                Spacer(modifier = Modifier.size(8.dp))
//                Icon(
//                    imageVector = Icons.Filled.Add,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onPrimary,
//                    modifier = Modifier
//                        .size(32.dp)
//                        .clickable {
//                            navigator.navigate(
//                                PlaningScreenDestination()
//                            )
//                        }
//                )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                SettingsSection(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp, horizontal = 8.dp),
                    settings = state.settings,
                    onSettingsChanged = {

                    },
                    locationList = listOf(Location(
                        locationId = null,
                        locationName = "Cieszyn",
                        coordinates = Coordinates(0.0, 0.0)
                        ),
                        Location(
                            locationId = null,
                            locationName = "Warszawa",
                            coordinates = Coordinates(0.0, 0.0)
                        ) )
//                    onSaveSettings = {
//
//                    },
//                    navigator = null
                )
            }
        }
    }
}