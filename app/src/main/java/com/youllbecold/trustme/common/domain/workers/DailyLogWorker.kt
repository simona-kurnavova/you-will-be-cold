package com.youllbecold.trustme.common.domain.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.ui.notifications.DailyLogNotification
import com.youllbecold.trustme.common.domain.notifications.DailyNotificationsManager
import com.youllbecold.trustme.common.domain.utils.DateTimeUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

/**
 * Worker class for showing notification prompting user to log their feelings daily.
 */
class DailyLogWorker(private val appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams), KoinComponent {

    private val dailyLogNotification: DailyLogNotification by inject()

    private val notificationsManager: DailyNotificationsManager by inject()

    override suspend fun doWork(): Result {
        if (!PermissionChecker.hasNotificationPermission(appContext)) {
            Log.d("DailyLogWorker", "Notification permission is missing, aborting")

            // Disable notification and cancel this worker
            notificationsManager.setDailyNotification(false)

            return Result.success()
        }

        Log.d("DailyLogWorker", "Showing daily log notification")
        dailyLogNotification.show()

        return Result.success()
    }

    companion object {
       private const val WORK_NAME = "DailyLogWorker"

        /**
         * Schedule the worker to run daily at the specified time.
         */
        fun schedule(
            context: Context,
            targetHour: Int = DEFAULT_HOUR_EXEC,
            targetMinute: Int = DEFAULT_MINUTE_EXEC
        ) {
            val delay = DateTimeUtils.calculateTimeUntilNext(targetHour, targetMinute)
            Log.d("DailyLogWorker", "Scheduling work with $delay ms at $targetHour:$targetMinute.")

            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()

            val request = PeriodicWorkRequestBuilder<DailyLogWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
        }

        /**
         * Cancel the worker.
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}

private const val DEFAULT_HOUR_EXEC = 20
private const val DEFAULT_MINUTE_EXEC = 0
