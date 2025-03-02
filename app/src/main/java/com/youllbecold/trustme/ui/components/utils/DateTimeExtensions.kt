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

/**
 * Time state holder.
 */
@Stable
data class TimeState(
    val hour: Int,
    val minute: Int,
) {
    /**
     * Converts time to [LocalTime].
     */
    val localTime: LocalTime
        get() = LocalTime.of(hour, minute)
}

/**
 * Date state holder.
 */
@Stable
data class DateState(
    val year: Int,
    val month: Int,
    val day: Int,
) {
    /**
     * Converts date to [LocalDate].
     */
    val localDate: LocalDate
        get() = LocalDate.of(year, month, day)

    /**
     * Converts date to milliseconds.
     */
    fun toMillis(): Long = localDate.toMillis()

    /**
     * Formats date to string.
     */
    fun formatDate(): String = localDate.formatDate()

    companion object {
        /**
         * Creates [DateState] from [LocalDate].
         */
        fun fromLocalDate(localDate: LocalDate): DateState {
            return DateState(
                year = localDate.year,
                month = localDate.monthValue,
                day = localDate.dayOfMonth
            )
        }
    }
}

/**
 * Date and time range state holder.
 */
@Stable
data class DateTimeState(
    val date: DateState,
    val timeFrom: TimeState,
    val timeTo: TimeState
) {
    companion object {
        /**
         * Creates [DateTimeState] from [LocalDateTime]s.
         */
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
