package com.youllbecold.trustme.worker

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

object WorkerUtils {

    /**
     * Calculates how much time (in milliseconds) is left until the next occurrence of the given time.
     *
     * @param targetHour The target hour (0-23).
     * @param targetMinute The target minute (0-59).
     * @return Time in milliseconds until the next occurrence of the given time.
     */
    fun calculateTimeUntilNext(targetHour: Int, targetMinute: Int): Long {
        val now = ZonedDateTime.now(ZoneId.systemDefault()) // Get the current local time

        // Create a reference for today at the desired time
        val todayTargetTime = now.withHour(targetHour).withMinute(targetMinute)

        // If the target time today has already passed, move to tomorrow
        val nextTargetTime = if (now.isBefore(todayTargetTime)) {
            todayTargetTime
        } else {
            todayTargetTime.plusDays(1)
        }

        // Return the time difference in milliseconds
        return ChronoUnit.MILLIS.between(now, nextTargetTime)
    }
}