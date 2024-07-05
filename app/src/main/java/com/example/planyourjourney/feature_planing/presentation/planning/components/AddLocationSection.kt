package com.example.planyourjourney.feature_planing.presentation.planning.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.presentation.planning.PlaningEvent
import com.example.planyourjourney.feature_planing.presentation.util.DecimalFormatter
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType

@Composable
fun AddLocationSection(
    modifier: Modifier = Modifier,
    searchInputType: SearchInputType,
    weatherCoordinates: Coordinates,
    weatherLocationName: String,
    onCoordinatesChanged: (Coordinates) -> Unit,
    onLocationNameChanged: (String) -> Unit,
    onAddLocation: () -> Unit,
    decimalFormatter: DecimalFormatter
) {
    Column(
        modifier
            .padding(8.dp)

    ) {
        when (searchInputType) {
            SearchInputType.LatitudeAndLongitude -> CoordinatesInputSection(
                modifier = Modifier.fillMaxWidth(),
                coordinates = weatherCoordinates,
                onValueChange = {
                    onCoordinatesChanged(it)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                decimalFormatter = decimalFormatter
            )

            SearchInputType.LocationName -> LocationNameInputSection(
                modifier = Modifier.fillMaxWidth(),
                locationName = weatherLocationName,
                onValueChange = {
                    onLocationNameChanged(it)
                }
            )
            //SearchInputType.Map -> Map(viewModel, Modifier.fillMaxSize())
        }

        Button(onClick = {
            onAddLocation()
        }) {
            Text(text = stringResource(id = R.string.add_location))
        }
    }
}