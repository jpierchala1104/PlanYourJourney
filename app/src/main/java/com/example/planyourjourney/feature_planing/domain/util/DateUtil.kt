package com.example.planyourjourney.feature_planing.domain.util

import kotlinx.datetime.LocalDateTime
import java.time.LocalDate

fun LocalDateTime.toLocalDate(): kotlinx.datetime.LocalDate {
    return kotlinx.datetime.LocalDate(date.year, date.month, date.dayOfMonth)
}

fun LocalDate.toKotlinLocalDate(): kotlinx.datetime.LocalDate {
    return kotlinx.datetime.LocalDate(year, month, dayOfMonth)
}