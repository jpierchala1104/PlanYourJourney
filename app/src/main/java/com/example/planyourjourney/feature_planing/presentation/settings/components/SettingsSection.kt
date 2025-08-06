package com.example.planyourjourney.feature_planing.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.R
import com.example.planyourjourney.core.presentation.DefaultRadioButton
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.util.PrecipitationUnits
import com.example.planyourjourney.feature_planing.domain.util.TemperatureUnits
import com.example.planyourjourney.feature_planing.domain.util.WindSpeedUnits
import com.example.planyourjourney.feature_planing.domain.util.Language
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun SettingsSection(
    modifier: Modifier = Modifier,
    settings: Settings = Settings(),
    onSettingsChanged: (Settings) -> Unit,
    locationList: List<Location>,
    //onSaveSettings: () -> Unit,
    //navigator: DestinationsNavigator?,
) {
    Column(modifier = modifier) {
        // TODO: another list or something for picking widgetLocation
        Text(
            text = stringResource(id = R.string.widget_location),
            style = MaterialTheme.typography.headlineMedium
        )
        Column (
            // TODO: modifier and max lines
        ){
            locationList.forEach {
                DefaultRadioButton(
                    text = it.locationName ?: stringResource(id = R.string.no_name),
                    selected = settings.widgetLocation == it,
                    onSelect = { onSettingsChanged(settings.copy(widgetLocation = it)) }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text(
            text = stringResource(id = R.string.language),
            style = MaterialTheme.typography.headlineMedium
        )
        Row {
            DefaultRadioButton(
                text = Language.English.name,
                selected = settings.language == Language.English,
                onSelect = { onSettingsChanged(settings.copy(language = Language.English)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = Language.Polski.name,
                selected = settings.language == Language.Polski,
                onSelect = { onSettingsChanged(settings.copy(language = Language.Polski)) }
            )
        }

        Text(
            text = stringResource(id = R.string.temperature_units),
            style = MaterialTheme.typography.headlineMedium
        )
        Row {
            DefaultRadioButton(
                text = "${stringResource(id = R.string.celsius)} ${TemperatureUnits.CELSIUS.displayUnits}",
                selected = settings.weatherUnits.temperatureUnits == TemperatureUnits.CELSIUS,
                onSelect = {
                    onSettingsChanged(
                        settings.copy(
                            weatherUnits = settings.weatherUnits.copy(
                                temperatureUnits = TemperatureUnits.CELSIUS
                            )
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "${stringResource(id = R.string.fahrenheit)} ${TemperatureUnits.FAHRENHEIT.displayUnits}",
                selected = settings.weatherUnits.temperatureUnits == TemperatureUnits.FAHRENHEIT,
                onSelect = {
                    onSettingsChanged(
                        settings.copy(
                            weatherUnits = settings.weatherUnits.copy(
                                temperatureUnits = TemperatureUnits.FAHRENHEIT
                            )
                        )
                    )
                }
            )
        }

        Text(
            text = stringResource(id = R.string.precipitation_units),
            style = MaterialTheme.typography.headlineMedium
        )
        Row {
            DefaultRadioButton(
                text = stringResource(id = R.string.millimeters),
                selected = settings.weatherUnits.precipitationUnits == PrecipitationUnits.MILLIMETERS,
                onSelect = {
                    onSettingsChanged(
                        settings.copy(
                            weatherUnits = settings.weatherUnits.copy(
                                precipitationUnits = PrecipitationUnits.MILLIMETERS
                            )
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = stringResource(id = R.string.inch),
                selected = settings.weatherUnits.precipitationUnits == PrecipitationUnits.INCH,
                onSelect = {
                    onSettingsChanged(
                        settings.copy(
                            weatherUnits = settings.weatherUnits.copy(
                                precipitationUnits = PrecipitationUnits.INCH
                            )
                        )
                    )
                }
            )
        }

        Text(
            text = stringResource(id = R.string.wind_speed_units),
            style = MaterialTheme.typography.headlineMedium
        )
        Column {
            Row {
                DefaultRadioButton(
                    text = stringResource(id = R.string.kilometers_per_hour),
                    selected = settings.weatherUnits.windSpeedUnits == WindSpeedUnits.KILOMETERS_PER_HOUR,
                    onSelect = {
                        onSettingsChanged(
                            settings.copy(
                                weatherUnits = settings.weatherUnits.copy(
                                    windSpeedUnits = WindSpeedUnits.KILOMETERS_PER_HOUR
                                )
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                DefaultRadioButton(
                    text = stringResource(id = R.string.meters_per_second),
                    selected = settings.weatherUnits.windSpeedUnits == WindSpeedUnits.METERS_PER_SECOND,
                    onSelect = {
                        onSettingsChanged(
                            settings.copy(
                                weatherUnits = settings.weatherUnits.copy(
                                    windSpeedUnits = WindSpeedUnits.METERS_PER_SECOND
                                )
                            )
                        )
                    }
                )
            }
            Row {
                DefaultRadioButton(
                    text = stringResource(id = R.string.miles_per_hour),
                    selected = settings.weatherUnits.windSpeedUnits == WindSpeedUnits.MILES_PER_HOUR,
                    onSelect = {
                        onSettingsChanged(
                            settings.copy(
                                weatherUnits = settings.weatherUnits.copy(
                                    windSpeedUnits = WindSpeedUnits.MILES_PER_HOUR
                                )
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                DefaultRadioButton(
                    text = stringResource(id = R.string.knots),
                    selected = settings.weatherUnits.windSpeedUnits == WindSpeedUnits.KNOTS,
                    onSelect = {
                        onSettingsChanged(
                            settings.copy(
                                weatherUnits = settings.weatherUnits.copy(
                                    windSpeedUnits = WindSpeedUnits.KNOTS
                                )
                            )
                        )
                    }
                )
            }
        }
    }
}