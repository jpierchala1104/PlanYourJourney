package com.example.planyourjourney.services

import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.model.lineSeries
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class ChartService {
    fun getCartesianChartModelProducer(
        chartSize: Int,
        chartDoubleValues: ArrayList<Double> = arrayListOf(),
        chartIntValues: ArrayList<Int> = arrayListOf()
    ): CartesianChartModelProducer {
        val cartesianChartModelProducer = CartesianChartModelProducer.build()
        cartesianChartModelProducer.tryRunTransaction {
            lineSeries { series((0 until chartSize).toList(),
                if(chartDoubleValues.isEmpty())
                chartIntValues
                else chartDoubleValues)
            }
        }
        return cartesianChartModelProducer
    }

    fun getBottomAxisFormatter(timeValues: ArrayList<String>): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
        val bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom> =
            AxisValueFormatter { x, _, _ ->
                //Date and Time String format from weather API -> 2024-02-05T00:00
                //The T in the middle make it so you can't parse it to any date time class/type
                //beforeT and afterT refer to the date and time cut from the APIs time string
            val dateTimeToFormat = timeValues[x.toInt()]
            val beforeT = dateTimeToFormat.substringBefore("T")
            val date = LocalDate.parse(beforeT)
            val afterT = dateTimeToFormat.substringAfter("T")
            if(afterT == "00:00")"${date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)}\n$afterT"
            else "\n$afterT"
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

    fun getMarkerLabelFormatter(timeValues: ArrayList<String>, unit: String): MarkerLabelFormatter{
        val markerLabelFormatter = MarkerLabelFormatter { markedEntries, _ ->
            val markedEntryTime = timeValues[markedEntries.first().entry.x.toInt()]
            val beforeT = markedEntryTime.substringBefore("T")
            val date = LocalDate.parse(beforeT)
            val afterT = markedEntryTime.substringAfter("T")
            //formatted output
            "${date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)}\n" +
            "${date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))} $afterT\n" +
            "${(markedEntries.first().entry as LineCartesianLayerModel.Entry).y}$unit"
        }
        return markerLabelFormatter
    }
}