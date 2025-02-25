package com.youllbecold.trustme.worker

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

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

    /**
     * Creates a periodic daily work request with the given delay.
     *
     * @param delay The delay in milliseconds.
     * @return The created work request.
     */
    fun createDailyWorkRequest(delay: Long): PeriodicWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return PeriodicWorkRequestBuilder<DailyRecommendWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()
    }
}