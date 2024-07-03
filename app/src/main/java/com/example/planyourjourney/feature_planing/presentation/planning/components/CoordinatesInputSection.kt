package com.example.planyourjourney.feature_planing.presentation.planning.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.presentation.util.DecimalFormatter

@Composable
fun CoordinatesInputSection(
    modifier: Modifier = Modifier,
    coordinates: Coordinates = Coordinates(0.0, 0.0),
    onValueChange: (Coordinates) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    decimalFormatter: DecimalFormatter = DecimalFormatter()
) {
    Column(
        modifier = modifier
    ) {
        DefaultTextField(
            text = coordinates.latitude.toString(),
            label = stringResource(id = R.string.latitude),
            onValueChange = {
                val latitude = decimalFormatter
                    .checkLatitudeRange(decimalFormatter.cleanup(it))
                onValueChange(
                    Coordinates(
                        if (latitude != "" && latitude != ".") latitude.toDouble()
                        else 0.0,
                        coordinates.longitude
                    )
                )
            },
            keyboardOptions = keyboardOptions
        )
        DefaultTextField(
            text = coordinates.longitude.toString(),
            label = stringResource(id = R.string.longitude),
            onValueChange = {
                val longitude = decimalFormatter
                    .checkLongitudeRange(decimalFormatter.cleanup(it))
                onValueChange(
                    Coordinates(
                        coordinates.latitude,
                        if (longitude != "" && longitude != ".") longitude.toDouble()
                        else 0.0
                    )
                )
            },
            keyboardOptions = keyboardOptions
        )
    }
}