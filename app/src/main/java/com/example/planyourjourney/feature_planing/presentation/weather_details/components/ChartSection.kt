package com.example.planyourjourney.feature_planing.presentation.weather_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.feature_planing.presentation.weather_details.ChartState

@Composable
fun ChartSection(
    modifier: Modifier = Modifier,
    chartStateList: List<ChartState>
    ) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(count = chartStateList.size) { i ->
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val chart = chartStateList[i]
                DefaultChart(
                    modifier = Modifier
                        .height(300.dp)
                        .padding(5.dp),
                    chartTitle = stringResource(id = chart.chartTitleResourceId),
                    modelProducer = chart.modelProducer,
                    markerLabelFormatter = chart.markerLabelFormatter,
                    startAxisValueFormatter = chart.startAxisValueFormatter,
                    bottomAxisValueFormatter = chart.bottomAxisValueFormatter
                )
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .size(30.dp)
                )
            }
        }
    }
}