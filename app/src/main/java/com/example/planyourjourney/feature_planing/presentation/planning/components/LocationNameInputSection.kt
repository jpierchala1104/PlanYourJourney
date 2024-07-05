package com.example.planyourjourney.feature_planing.presentation.planning.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
            text = locationName,
            label = stringResource(id = R.string.location_name),
            onValueChange = { onValueChange(it) }
        )
    }
}
