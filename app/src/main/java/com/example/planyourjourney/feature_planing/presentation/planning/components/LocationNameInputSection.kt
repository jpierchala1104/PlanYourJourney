package com.example.planyourjourney.feature_planing.presentation.planning.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.example.planyourjourney.R

@Composable
fun LocationNameInputSection(
    modifier: Modifier = Modifier,
    locationName: String = "",
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        DefaultTextField(
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            text = locationName,
            label = stringResource(id = R.string.add_location_label),
            onValueChange = { onValueChange(it) }
        )
    }
}
