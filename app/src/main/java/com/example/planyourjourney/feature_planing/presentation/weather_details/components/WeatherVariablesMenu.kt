package com.example.planyourjourney.feature_planing.presentation.weather_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.WeatherVariables

@Composable
fun WeatherVariablesMenu(
    modifier: Modifier = Modifier,
    isWeatherVariablesSectionVisible: Boolean,
    weatherVariables: WeatherVariables,
    onToggleWeatherVariablesSection: () -> Unit,
    onWeatherVariablesChanged: (WeatherVariables) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, 0.dp, 0.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.pick_weather_variables),
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(
            onClick = {
                onToggleWeatherVariablesSection()
            }
        ) {
            if (isWeatherVariablesSectionVisible) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null
                )
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }
    }
    AnimatedVisibility(
        visible = isWeatherVariablesSectionVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        WeatherVariablesSelectionSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            weatherVariables = weatherVariables,
            onWeatherVariablesChange = {
                onWeatherVariablesChanged(it)
            }
        )
    }

}