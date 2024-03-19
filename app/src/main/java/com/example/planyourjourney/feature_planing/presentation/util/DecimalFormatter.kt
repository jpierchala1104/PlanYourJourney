package com.example.planyourjourney.feature_planing.presentation.util

import java.text.DecimalFormatSymbols

class DecimalFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {
    private val decimalSeparator = symbols.decimalSeparator

    fun cleanup(input: String): String {
        if (input.matches("\\D".toRegex())) return ""
        if (input.matches("0+".toRegex())) return "0"

        val sb = StringBuilder()

        var hasDecimalSep = false
        if (input.first() == '-') sb.append('-')

        for (char in input) {
            if (char.isDigit()) {
                sb.append(char)
                continue
            }
            if (char == decimalSeparator && !hasDecimalSep && sb.isNotEmpty()) {
                sb.append(char)
                hasDecimalSep = true
            }
        }

        return limitTo4Decimals(sb.toString())
    }

    private fun limitTo4Decimals(input: String): String{
        if(!input.contains('.')) return input
        val beforeDecimal = input.substringBefore('.')
        val afterDecimal = input.substringAfter('.')
        return "$beforeDecimal.${afterDecimal.take(4)}"
    }

    fun checkLatitudeRange(input: String): String{
        if (input == "" || input == ".") return ""
        val inputDouble = input.toDouble()
        if(-90<=inputDouble && inputDouble<=90) return input
        if (inputDouble<-90) return "-90"
        if (inputDouble>90) return "90"
        return ""
    }

    fun checkLongitudeRange(input: String): String{
        if (input == "" || input == ".") return ""
        val inputDouble = input.toDouble()
        if(-180<=inputDouble && inputDouble<180) return input
        if (inputDouble<-180) return "-180"
        if (inputDouble>=179.9375) return "179.9374"
        return ""
    }
}