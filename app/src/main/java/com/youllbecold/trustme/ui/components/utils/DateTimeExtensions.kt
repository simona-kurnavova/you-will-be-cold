package com.youllbecold.trustme.ui.components.utils

import androidx.compose.runtime.Stable
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
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
 * Converts milliseconds to [LocalDateTime].
 */
val LocalDateTime.dateTimeToMillis: Long
    get() = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

/**
 * Converts milliseconds to [LocalDateTime].
 */
val Long.millisToDateTime: LocalDateTime
    get() = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

/**
 * Converts [LocalDate] to milliseconds.
 */
fun LocalDate.toMillis(): Long = atTime(LocalTime.NOON) // Set time to noon to avoid timezone shifts
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()

@Stable
data class TimeState(
    val hour: Int,
    val minute: Int,
) {
    val time: LocalTime
        get() = LocalTime.of(hour, minute)
}

@Stable
data class DateState(
    val year: Int,
    val month: Int,
    val day: Int,
) {
    val date: LocalDate
        get() = LocalDate.of(year, month, day)

    fun toMillis(): Long = date.toMillis()

    fun formatDate(): String = date.formatDate()

    companion object {
        fun fromMillis(millis: Long): DateState {
            val localDate = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            return DateState(
                year = localDate.year,
                month = localDate.monthValue,
                day = localDate.dayOfMonth
            )
        }

        fun fromLocalDate(localDate: LocalDate): DateState {
            return DateState(
                year = localDate.year,
                month = localDate.monthValue,
                day = localDate.dayOfMonth
            )
        }
    }
}

@Stable
data class DateTimeState(
    val date: DateState,
    val timeFrom: TimeState,
    val timeTo: TimeState
) {
    companion object {
        fun fromDateTime(
            dateTimeFrom: LocalDateTime,
            datetimeTo: LocalDateTime
        ): DateTimeState {
            return DateTimeState(
                date = DateState.fromLocalDate(dateTimeFrom.toLocalDate()),
                timeFrom = TimeState(
                    hour = dateTimeFrom.hour,
                    minute = dateTimeFrom.minute
                ),
                timeTo = TimeState(
                    hour = datetimeTo.hour,
                    minute = datetimeTo.minute
                )
            )
        }
    }
}
