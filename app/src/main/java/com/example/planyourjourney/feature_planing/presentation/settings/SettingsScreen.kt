package com.example.planyourjourney.feature_planing.presentation.settings

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planyourjourney.R
import com.example.planyourjourney.core.presentation.AppToolbar
import com.example.planyourjourney.feature_planing.presentation.destinations.WeatherScreenDestination
import com.example.planyourjourney.feature_planing.presentation.settings.components.SettingsSection
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun SettingsScreen(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
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
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.List,
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier
//                        .size(32.dp)
//                        .clickable {
//                            navigator.navigate(
//                                WeatherScreenDestination()
//                            )
//                        }
//                )
//                Spacer(modifier = Modifier.size(8.dp))
//                Icon(
//                    imageVector = Icons.Filled.Settings,
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier
//                        .size(32.dp)
//                        .clickable {
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
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                settings = state.settings,
                onSettingsSaved = {
                    viewModel.onEvent(SettingsEvent.SettingsSaved(it))
                }
            )
        }
    }
}