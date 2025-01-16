package com.example.planyourjourney.feature_planing.presentation.planning.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.presentation.planning.PlaningEvent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun LocationList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isLocationLoaded: Boolean,
    locationList: List<Location>,
    onDeleteLocation: (Location) -> Unit,
    navigator: DestinationsNavigator?
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.locations),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .wrapContentSize()
                        .size(56.dp),
                    strokeWidth = 6.dp,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            } else {
                if (!isLocationLoaded || locationList.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(id = R.string.locations_empty)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(count = locationList.size) { i ->
                            val location = locationList[i]
                            LocationItem(
                                location = location,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navigator!!.navigate(
                                            com.example.planyourjourney.feature_planing.presentation.destinations.WeatherDetailsScreenDestination(
                                                location.locationId!!
                                            )
                                        )
                                    },
                                //.padding(16.dp)
                                onDeleteClick = {
                                    // TODO: Maybe change this to confirm delete scaffoldState
                                    //  with no restore option
                                    onDeleteLocation(location)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}