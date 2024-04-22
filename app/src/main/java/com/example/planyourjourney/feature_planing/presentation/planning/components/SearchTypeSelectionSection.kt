package com.example.planyourjourney.feature_planing.presentation.planning.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.R
import com.example.planyourjourney.core.presentation.DefaultRadioButton
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType

@Composable
fun SearchTypeSelectionSection(
    modifier: Modifier = Modifier,
    searchInputType: SearchInputType,
    onSearchInputTypeChange: (SearchInputType) -> Unit
) {
    Column(modifier = modifier) {
        Row {
            DefaultRadioButton(
                text = stringResource(id = R.string.location_name),
                selected = searchInputType == SearchInputType.LocationName,
                onSelect = { onSearchInputTypeChange(SearchInputType.LocationName) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = stringResource(id = R.string.latitude_and_longitude),
                selected = searchInputType == SearchInputType.LatitudeAndLongitude,
                onSelect = { onSearchInputTypeChange(SearchInputType.LatitudeAndLongitude) }
            )
        }
    }
}