package com.example.planyourjourney.feature_planing.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.core.presentation.DefaultRadioButton
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.util.PrecipitationUnits
import com.example.planyourjourney.feature_planing.domain.util.TemperatureUnits
import com.example.planyourjourney.feature_planing.domain.util.WindSpeedUnits
import com.example.planyourjourney.feature_planing.presentation.util.Language

@Composable
fun SettingsSection(
    modifier: Modifier = Modifier,
    settings: Settings = Settings(),
    onSettingsSaved: (Settings) -> Unit
    ) {
    var newSettings = Settings()
    Column(modifier = modifier) {
        Text(text = "Language", style = MaterialTheme.typography.headlineMedium)
        Row {
            DefaultRadioButton(
                text = Language.English.name,
                selected = settings.language == Language.English ,
                onSelect = { newSettings = newSettings.copy(language = Language.English) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = Language.Polski.name,
                selected = settings.language == Language.Polski,
                onSelect = { newSettings = newSettings.copy(language = Language.Polski) }
            )
        }

        Text(text = "Temperature Units", style = MaterialTheme.typography.headlineMedium)
        Row {
            DefaultRadioButton(
                text = "${TemperatureUnits.CELSIUS.name} ${TemperatureUnits.CELSIUS.displayUnits}",
                selected = settings.weatherUnits.temperatureUnits == TemperatureUnits.CELSIUS ,
                onSelect = {
                    newSettings = newSettings.copy(
                        weatherUnits = newSettings.weatherUnits.copy(
                            temperatureUnits = TemperatureUnits.CELSIUS
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "${TemperatureUnits.FAHRENHEIT.name} ${TemperatureUnits.FAHRENHEIT.displayUnits}",
                selected = settings.weatherUnits.temperatureUnits == TemperatureUnits.FAHRENHEIT,
                onSelect = {
                    newSettings = newSettings.copy(
                        weatherUnits = newSettings.weatherUnits.copy(
                            temperatureUnits = TemperatureUnits.FAHRENHEIT
                        )
                    )
                }
            )
        }

        Text(text = "Precipitation Units", style = MaterialTheme.typography.headlineMedium)
        Row {
            DefaultRadioButton(
                text = PrecipitationUnits.MILLIMETERS.name,
                selected = settings.weatherUnits.precipitationUnits == PrecipitationUnits.MILLIMETERS ,
                onSelect = {
                    newSettings = newSettings.copy(
                        weatherUnits = newSettings.weatherUnits.copy(
                            precipitationUnits = PrecipitationUnits.MILLIMETERS
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = PrecipitationUnits.INCH.name,
                selected = settings.weatherUnits.precipitationUnits == PrecipitationUnits.INCH,
                onSelect = {
                    newSettings = newSettings.copy(
                        weatherUnits = newSettings.weatherUnits.copy(
                            precipitationUnits = PrecipitationUnits.INCH
                        )
                    )
                }
            )
        }

        Text(text = "Wind Speed Units", style = MaterialTheme.typography.headlineMedium)
        Row {
            DefaultRadioButton(
                text = WindSpeedUnits.KILOMETERS_PER_HOUR.name,
                selected = settings.weatherUnits.windSpeedUnits == WindSpeedUnits.KILOMETERS_PER_HOUR ,
                onSelect = {
                    newSettings = newSettings.copy(
                        weatherUnits = newSettings.weatherUnits.copy(
                            windSpeedUnits = WindSpeedUnits.KILOMETERS_PER_HOUR
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = WindSpeedUnits.METERS_PER_SECOND.name,
                selected = settings.weatherUnits.windSpeedUnits == WindSpeedUnits.METERS_PER_SECOND,
                        onSelect = {
                    newSettings = newSettings.copy(
                        weatherUnits = newSettings.weatherUnits.copy(
                            windSpeedUnits = WindSpeedUnits.METERS_PER_SECOND
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = WindSpeedUnits.MILES_PER_HOUR.name,
                selected = settings.weatherUnits.windSpeedUnits == WindSpeedUnits.MILES_PER_HOUR,
                onSelect = {
                    newSettings = newSettings.copy(
                        weatherUnits = newSettings.weatherUnits.copy(
                            windSpeedUnits = WindSpeedUnits.MILES_PER_HOUR
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = WindSpeedUnits.KNOTS.name,
                selected = settings.weatherUnits.windSpeedUnits == WindSpeedUnits.KNOTS,
                onSelect = {
                    newSettings = newSettings.copy(
                        weatherUnits = newSettings.weatherUnits.copy(
                            windSpeedUnits = WindSpeedUnits.KNOTS
                        )
                    )
                }
            )
        }
        // TODO: style the button and test
        Button(onClick = { onSettingsSaved(newSettings) }) {
            Text(text = "Save settings")
        }
    }
}