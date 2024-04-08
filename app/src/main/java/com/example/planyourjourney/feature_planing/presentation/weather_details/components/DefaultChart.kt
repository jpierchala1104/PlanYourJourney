package com.example.planyourjourney.feature_planing.presentation.weather_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planyourjourney.core.presentation.rememberMarker
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.marker.DefaultMarkerLabelFormatter
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer

@Composable
fun DefaultChart(
    modifier: Modifier = Modifier,
    chartTitle: String = "",
    modelProducer: CartesianChartModelProducer,
    markerLabelFormatter: MarkerLabelFormatter = DefaultMarkerLabelFormatter(),
    startAxisValueFormatter: AxisValueFormatter<AxisPosition.Vertical.Start>,
    bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = chartTitle,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .wrapContentHeight()
        )
        CartesianChartHost(
            modifier = Modifier.fillMaxHeight(),
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(),
                rememberLineCartesianLayer(
                    spacing = 8.dp,
                    lines = listOf(rememberLineSpec(DynamicShaders.color(MaterialTheme.colorScheme.primary)))
                ),
                startAxis = rememberStartAxis(
                    itemPlacer = remember { AxisItemPlacer.Vertical.count(count = { 6 }) },
                    valueFormatter = startAxisValueFormatter
                ),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = bottomAxisValueFormatter,
                    itemPlacer = AxisItemPlacer.Horizontal.default(
                        spacing = 6,
                        addExtremeLabelPadding = true
                    ),
                    label = rememberAxisLabelComponent(
                        lineCount = 2
                    ),
                ),
                fadingEdges = rememberFadingEdges()
            ),
            modelProducer = modelProducer,
            horizontalLayout = HorizontalLayout.fullWidth(),
            marker = rememberMarker(
                markerLabelFormatter = markerLabelFormatter
            )
        )
    }
}