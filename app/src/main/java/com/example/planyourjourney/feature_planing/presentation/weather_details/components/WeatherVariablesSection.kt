package com.example.planyourjourney.feature_planing.presentation.weather_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.R
import com.example.planyourjourney.core.presentation.DefaultCheckbox
import com.example.planyourjourney.feature_planing.domain.model.WeatherVariables

@Composable
fun WeatherVariablesSection(
    modifier: Modifier = Modifier,
    weatherVariables: WeatherVariables,
    onWeatherVariablesChange: (WeatherVariables) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        DefaultCheckbox(
            text = stringResource(id = R.string.temperature),
            checked = weatherVariables.isTemperature2mChecked,
            onCheck = {
                onWeatherVariablesChange(weatherVariables.copy(isTemperature2mChecked = it))
            })
        Spacer(modifier = Modifier.width(8.dp))
        DefaultCheckbox(
            text = stringResource(id = R.string.precipitation_probability),
            checked = weatherVariables.isPrecipitationProbabilityChecked,
            onCheck = {
                onWeatherVariablesChange(weatherVariables.copy(isPrecipitationProbabilityChecked = it))
            })
        Spacer(modifier = Modifier.width(8.dp))
        DefaultCheckbox(
            text = stringResource(id = R.string.rain),
            checked = weatherVariables.isRainChecked,
            onCheck = {
                onWeatherVariablesChange(weatherVariables.copy(isRainChecked = it))
            })
        Spacer(modifier = Modifier.width(8.dp))
        DefaultCheckbox(
            text = stringResource(id = R.string.snowfall),
            checked = weatherVariables.isSnowfallChecked,
            onCheck = {
                onWeatherVariablesChange(weatherVariables.copy(isSnowfallChecked = it))
            })
        Spacer(modifier = Modifier.width(8.dp))
        DefaultCheckbox(
            text = stringResource(id = R.string.cloud_cover),
            checked = weatherVariables.isCloudCoverChecked,
            onCheck = {
                onWeatherVariablesChange(weatherVariables.copy(isCloudCoverChecked = it))
            })
        Spacer(modifier = Modifier.width(8.dp))
        DefaultCheckbox(
            text = stringResource(id = R.string.relative_humidity),
            checked = weatherVariables.isRelativeHumidity2mChecked,
            onCheck = {
                onWeatherVariablesChange(weatherVariables.copy(isRelativeHumidity2mChecked = it))
            })
        Spacer(modifier = Modifier.width(8.dp))
        DefaultCheckbox(
            text = stringResource(id = R.string.wind_speed),
            checked = weatherVariables.isWindSpeed10mChecked,
            onCheck = {
                onWeatherVariablesChange(weatherVariables.copy(isWindSpeed10mChecked = it))
            })
    }
}