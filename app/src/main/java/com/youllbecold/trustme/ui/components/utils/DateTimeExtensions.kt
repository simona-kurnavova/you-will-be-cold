package com.youllbecold.trustme.ui.components.utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Formats time to string in short format.
 */
fun LocalTime.formatTime(): String {
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    return this.format(formatter)
}

/**
 * Formats date to string in long format.
 */
fun LocalDate.formatDate(): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    return this.format(formatter)
}

/**
 * Converts [LocalDate] to milliseconds.
 */
fun ImmutableDate.toMillis(): Long = date.atTime(LocalTime.NOON) // Set time to noon to avoid timezone shifts
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()
