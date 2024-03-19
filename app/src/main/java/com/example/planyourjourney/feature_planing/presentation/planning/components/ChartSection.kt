package com.example.planyourjourney.feature_planing.presentation.planning.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.feature_planing.presentation.planning.ChartState

@Composable
fun ChartSection(
    modifier: Modifier = Modifier,
    chartStateList: List<ChartState>
    ) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        chartStateList.forEach {
            DefaultChart(
                modifier = Modifier.height(300.dp).padding(5.dp),
                chartTitle = it.chartTitle,
                modelProducer = it.modelProducer,
                markerLabelFormatter = it.markerLabelFormatter,
                startAxisValueFormatter = it.startAxisValueFormatter,
                bottomAxisValueFormatter = it.bottomAxisValueFormatter
            )
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .size(30.dp)
            )
        }
    }
}