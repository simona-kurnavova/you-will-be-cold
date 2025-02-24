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

            val delay = WorkerUtils.calculateTimeUntilNext(targetHour, targetMinute)

            Log.d("DailyLogWorker", "Scheduling work in $delay minutes at $targetHour:$targetMinute daily")

            val request = PeriodicWorkRequestBuilder<DailyLogWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
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
    }
}