package com.example.planyourjourney.services

import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class ChartService {
    fun getChartEntryModelProducer(
        chartSize: Int,
        chartDoubleValues: ArrayList<Double> = arrayListOf(),
        chartIntValues: ArrayList<Int> = arrayListOf()
    ): ChartEntryModelProducer {
        val chartEntryModelProducer = ChartEntryModelProducer()
        val dataPoints = arrayListOf<FloatEntry>()
        for (i in 0 until chartSize) {
            dataPoints
                .add(
                    FloatEntry(
                        x = i.toFloat(),
                        y = if(chartDoubleValues.isEmpty())
                            chartIntValues[i].toFloat()
                        else chartDoubleValues[i].toFloat()
                    )
                )
        }
        chartEntryModelProducer.setEntries(listOf(dataPoints))
        return chartEntryModelProducer
    }

    fun getBottomAxisFormatter(timeValues: ArrayList<String>): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
        val bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom> =
            AxisValueFormatter { x, _ ->
            val beforeT = timeValues[x.toInt()].substringBefore("T")
            val date = LocalDate.parse(beforeT)
            val afterT = timeValues[x.toInt()].substringAfter("T")
            if(afterT == "00:00")"${date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)}\n$afterT"
            else "\n$afterT"
        }
        return bottomAxisValueFormatter
    }
}