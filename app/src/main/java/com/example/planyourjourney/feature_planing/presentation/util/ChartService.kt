package com.example.planyourjourney.feature_planing.presentation.util

import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.model.columnSeries
import com.patrykandpatrick.vico.core.model.lineSeries
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class ChartService {
    fun getCartesianLineChartModelProducer(
        chartSize: Int,
        chartDoubleValues: List<Double> = arrayListOf(),
        chartIntValues: List<Int> = arrayListOf()
    ): CartesianChartModelProducer {
        val cartesianChartModelProducer = CartesianChartModelProducer.build()
        cartesianChartModelProducer.tryRunTransaction {
            lineSeries {
                series((0 until chartSize).toList(),
                    chartDoubleValues.ifEmpty { chartIntValues })
            }
        }
        return cartesianChartModelProducer
    }

    fun getCartesianColumnChartModelProducer(
        chartSize: Int,
        chartDoubleValues: List<Double> = arrayListOf(),
        chartIntValues: List<Int> = arrayListOf()
    ): CartesianChartModelProducer {
        val cartesianChartModelProducer = CartesianChartModelProducer.build()
        cartesianChartModelProducer.tryRunTransaction {
            columnSeries {
                series((0 until chartSize).toList(),
                    chartDoubleValues.ifEmpty { chartIntValues })
            }
        }
        return cartesianChartModelProducer
    }

    fun getBottomAxisFormatter(hourlyWeather: List<HourlyWeather>): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
        val bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom> =
            AxisValueFormatter { x, _, _ ->
                val dateTime = hourlyWeather[x.toInt()].time
                "${if (dateTime.hour == 0) dateTime.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.ENGLISH
                ) else "" }\n${dateTime.hour}:00"
            }
        return bottomAxisValueFormatter
    }

    fun getStartAxisFormatter(unit: String): AxisValueFormatter<AxisPosition.Vertical.Start> {
        val startAxisValueFormatter: AxisValueFormatter<AxisPosition.Vertical.Start> =
            AxisValueFormatter { x, _, _ ->
                "${x.toInt()}$unit"
            }
        return startAxisValueFormatter
    }

    fun getMarkerLabelFormatter(
        hourlyWeather: List<HourlyWeather>,
        unit: String
    ): MarkerLabelFormatter {
        val markerLabelFormatter = MarkerLabelFormatter { markedEntries, _ ->
            val markedEntryTime = hourlyWeather[markedEntries.first().entry.x.toInt()].time
            //formatted output
            "${markedEntryTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)}\n" +
            "${markedEntryTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))} ${markedEntryTime.hour}:00\n" +
            "${(markedEntries.first().entry as LineCartesianLayerModel.Entry).y}$unit"
        }
        return markerLabelFormatter
    }
}