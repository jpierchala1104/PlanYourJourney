package com.example.planyourjourney.feature_planing.presentation.planning.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
            text = locationName,
            label = "Location Name",
            onValueChange = { onValueChange(it) }
        )
    }
}
