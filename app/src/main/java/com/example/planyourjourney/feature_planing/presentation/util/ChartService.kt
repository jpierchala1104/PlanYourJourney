package com.example.planyourjourney.feature_planing.presentation.util

import com.example.planyourjourney.feature_planing.presentation.weather_details.ChartState
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.model.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.model.columnSeries
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class ChartService{
    private var locale: Locale = Locale(Locale.ENGLISH.language)
    fun setLocale(newLocale: Locale){
        locale = newLocale
    }
    private fun getCartesianLineChartModelProducer(
        chartSize: Int,
        chartValues: List<Double> = arrayListOf()
    ): CartesianChartModelProducer {
        val cartesianChartModelProducer = CartesianChartModelProducer.build()
        cartesianChartModelProducer.tryRunTransaction {
            lineSeries {
                series((0 until chartSize).toList(),
                    chartValues)
            }
        }
        return cartesianChartModelProducer
    }

    private fun getCartesianColumnChartModelProducer(
        chartSize: Int,
        chartValues: List<Double> = arrayListOf()
    ): CartesianChartModelProducer {
        val cartesianChartModelProducer = CartesianChartModelProducer.build()
        cartesianChartModelProducer.tryRunTransaction {
            columnSeries {
                series((0 until chartSize).toList(),
                    chartValues)
            }
        }
        return cartesianChartModelProducer
    }

    private fun getBottomAxisFormatter(
        localDateTimeList: List<LocalDateTime>
    ): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
        val bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom> =
            AxisValueFormatter { x, _, _ ->
                val dateTime = localDateTimeList[x.toInt()]
                "${if (dateTime.hour == 0) dateTime.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    locale
                ) else "" }\n${dateTime.hour}:00"
            }
        return bottomAxisValueFormatter
    }

    private fun getStartAxisFormatter(unit: String): AxisValueFormatter<AxisPosition.Vertical.Start> {
        val startAxisValueFormatter: AxisValueFormatter<AxisPosition.Vertical.Start> =
            AxisValueFormatter { x, _, _ ->
                "${x.toInt()}$unit"
            }
        return startAxisValueFormatter
    }

    private fun getMarkerLabelFormatter(
        localDateTimeList: List<LocalDateTime>,
        unit: String,
        isLineCartesianChart: Boolean = true
    ): MarkerLabelFormatter {
        val markerLabelFormatter = MarkerLabelFormatter { markedEntries, _ ->
            val markedEntryTime = localDateTimeList[markedEntries.first().entry.x.toInt()]
            //formatted output
            "${markedEntryTime.dayOfWeek.getDisplayName(TextStyle.FULL, locale)}\n" +
            "${markedEntryTime.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))} ${markedEntryTime.hour}:00\n" +
            if(isLineCartesianChart){
                "${(markedEntries.first().entry as LineCartesianLayerModel.Entry).y}$unit"
            } else {
                "${(markedEntries.first().entry as ColumnCartesianLayerModel.Entry).y}$unit"
            }
        }
        return markerLabelFormatter
    }

    fun getLineChartState(
        chartTitleResourceId: Int,
        chartValuesList: List<Double>,
        localDateTimeList: List<LocalDateTime>,
        unit: String
    ): ChartState{
        return ChartState(
            chartTitleResourceId = chartTitleResourceId,
            modelProducer = getCartesianLineChartModelProducer(
                chartSize = chartValuesList.size,
                chartValues = chartValuesList
            ),
            markerLabelFormatter = getMarkerLabelFormatter(
                localDateTimeList = localDateTimeList,
                unit = unit
            ),
            startAxisValueFormatter = getStartAxisFormatter(
                unit = unit
            ),
            bottomAxisValueFormatter = getBottomAxisFormatter(
                localDateTimeList = localDateTimeList
            )
        )
    }

    fun getColumnChartState(
        chartTitleResourceId: Int,
        chartValuesList: List<Double>,
        localDateTimeList: List<LocalDateTime>,
        unit: String
    ): ChartState{
        return ChartState(
            chartTitleResourceId = chartTitleResourceId,
            modelProducer = getCartesianColumnChartModelProducer(
                chartSize = chartValuesList.size,
                chartValues = chartValuesList
            ),
            markerLabelFormatter = getMarkerLabelFormatter(
                localDateTimeList = localDateTimeList,
                unit = unit,
                isLineCartesianChart = false
            ),
            startAxisValueFormatter = getStartAxisFormatter(
                unit = unit
            ),
            bottomAxisValueFormatter = getBottomAxisFormatter(
                localDateTimeList = localDateTimeList
            )
        )
    }
}