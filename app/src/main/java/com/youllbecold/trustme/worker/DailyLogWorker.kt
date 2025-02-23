package com.youllbecold.trustme.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.youllbecold.trustme.notifications.NotificationHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class DailyLogWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams), KoinComponent {

    private val notificationHelper: NotificationHelper by inject()

    override fun doWork(): Result {
        Log.d("DailyLogWorker", "Showing daily log notification")
        notificationHelper.showDailyLogNotification()

        return Result.success()
    }

    companion object {
        const val WORK_NAME = "DailyLogWorker"

        fun schedule(context: Context, targetHour: Int = 20, targetMinute: Int = 0) {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val delay = calculateTimeUntilNext(targetHour, targetMinute)

            Log.d("DailyLogWorker", "Scheduling work in $delay minutes at $targetHour:$targetMinute daily")

            val request = PeriodicWorkRequestBuilder<DailyLogWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(delay, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }

        /**
         * Calculates how much time (in milliseconds) is left until the next occurrence of the given time.
         *
         * @param targetHour The target hour (0-23).
         * @param targetMinute The target minute (0-59).
         * @return Time in milliseconds until the next occurrence of the given time.
         */
        private fun calculateTimeUntilNext(targetHour: Int, targetMinute: Int): Long {
            val now = ZonedDateTime.now(ZoneId.systemDefault()) // Get the current local time

            // Create a reference for today at the desired time
            val todayTargetTime = now.withHour(targetHour).withMinute(targetMinute)
                .withSecond(0).withNano(0)

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
}