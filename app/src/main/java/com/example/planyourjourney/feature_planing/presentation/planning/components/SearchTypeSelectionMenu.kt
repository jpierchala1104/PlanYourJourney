package com.example.planyourjourney.feature_planing.presentation.planning.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.planyourjourney.core.presentation.DefaultRadioButton
import com.example.planyourjourney.feature_planing.presentation.util.SearchInputType

@Composable
fun SearchTypeSelectionMenu(
    modifier: Modifier = Modifier,
    onToggleSearchInputTypeSelection: () -> Unit,
    isSearchInputTypeSelectionSectionVisible: Boolean,
    searchInputType: SearchInputType,
    onSearchInputTypeChange: (SearchInputType) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 0.dp, 0.dp, 0.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(5f),
                text = stringResource(id = R.string.search_input_type),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    onToggleSearchInputTypeSelection()
                }
            ) {
                if (isSearchInputTypeSelectionSectionVisible) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isSearchInputTypeSelectionSectionVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
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
}