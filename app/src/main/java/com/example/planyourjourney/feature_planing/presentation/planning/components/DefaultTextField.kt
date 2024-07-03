package com.example.planyourjourney.feature_planing.presentation.planning.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DefaultTextField(
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            keyboardOptions = keyboardOptions
        )
    }
}

@Preview
@Composable
fun DefaultTextFieldPreview(){
    DefaultTextField(text = "Latitude", label = "Latitude", onValueChange = {})
}

//onValueChange = {
//    latitude = decimalFormatter.checkLatitudeRange(decimalFormatter.cleanup(it))
//    if (latitude != "" && latitude != ".")
//        coordinatesState.latitude = latitude.toDouble()
//    else
//        coordinatesState.latitude = 0.0
//},
//        label = { Text(text = "Latitude") },
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)