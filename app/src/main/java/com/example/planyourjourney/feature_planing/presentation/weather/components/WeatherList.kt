package com.example.planyourjourney.feature_planing.presentation.weather.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import java.util.Locale

@Composable
fun WeatherList(
    modifier: Modifier = Modifier,
    locationWeatherList: List<LocationWeather>,
    isLoading: Boolean,
    onRefreshLocation: (Location) -> Unit,
    scope: CoroutineScope,
    weatherUnits: WeatherUnits,
    localeCode: String,
    navigator: DestinationsNavigator?
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.weather_forecast),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .size(8.dp))

        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .wrapContentSize()
                    .size(72.dp),
                strokeWidth = 6.dp,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        } else {
            if (locationWeatherList.isEmpty()) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.locations_empty),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                    navigator!!.navigate(
                        com.example.planyourjourney.feature_planing.presentation.destinations.PlanningScreenDestination()
                    )
                }) {
                    Text(text = stringResource(id = R.string.add_location))
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(count = locationWeatherList.size) { i ->
                        val location = locationWeatherList[i]
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize()
                                .clickable {
                                    navigator!!.navigate(
                                        com.example.planyourjourney.feature_planing.presentation.destinations.WeatherDetailsScreenDestination(
                                            location.location.locationId!!
                                        )
                                    )
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp, 0.dp, 8.dp, 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column (modifier = Modifier.fillMaxWidth(0.8f)){
                                    Text(
                                        text = location.location.locationName.orEmpty(),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
//                                    Text(
//                                        text = stringResource(id = R.string.latitude_colon) +
//                                                location.location.coordinates.latitude.toString(),
//                                        maxLines = 1,
//                                        overflow = TextOverflow.Ellipsis
//                                    )
//                                    Text(
//                                        text = stringResource(id = R.string.longitude_colon) +
//                                                location.location.coordinates.longitude.toString(),
//                                        maxLines = 1,
//                                        overflow = TextOverflow.Ellipsis
//                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clickable {
                                            onRefreshLocation(location.location)
                                        }
                                )
                            }
                            WeatherCard(
                                scope = scope,
                                locationWeather = location,
                                weatherUnits = weatherUnits,
                                locale = Locale(localeCode)
                            )
                        }
                    }
                }
            }
        }
    }
}