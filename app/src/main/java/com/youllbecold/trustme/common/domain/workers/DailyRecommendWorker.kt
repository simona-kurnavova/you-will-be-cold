package com.youllbecold.trustme.common.domain.workers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.youllbecold.trustme.common.ui.notifications.DailyRecommendNotification
import com.youllbecold.trustme.common.domain.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.notifications.DailyNotificationsManager
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.common.domain.utils.DateTimeUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

/**
 * Worker class for showing notification with daily weather recommendation.
 */
class DailyRecommendWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams), KoinComponent {

    private val currentWeatherUseCase: CurrentWeatherUseCase by inject()

    private val dailyRecommendNotification: DailyRecommendNotification by inject()

    private val unitsManager: UnitsManager by inject()

    private val notificationsManager: DailyNotificationsManager by inject()

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        if (!PermissionChecker.hasNotificationPermission(appContext) ||
            !PermissionChecker.hasLocationPermission(appContext)) {
            Log.d("DailyRecommendWorker", "Some permissions are missing, aborting")

            // Disable notification and cancel this worker
            notificationsManager.setRecommendNotification(false)
            return Result.success()
        }

        val weatherWithStatus = currentWeatherUseCase.fetchCurrentWeather(
            useCelsius = unitsManager.fetchUnitsCelsius()
        )
        val weather = weatherWithStatus.weather

        if (weatherWithStatus.status.isError() || weather == null) {
            Log.d("DailyRecommendWorker", "Weather status is ${weatherWithStatus.status}, aborting")
            return Result.success()
        }

        Log.d("DailyRecommendWorker", "Showing notification")
        dailyRecommendNotification.show(
            temperature = weather.temperature,
            useCelsiusUnits = weather.unitsCelsius,
            weatherEvaluation = weather.weatherEvaluation
        )

        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "DailyRecommendWorker"

        /**
         * Schedule the worker to run daily at the specified time.
         */
        fun schedule(
            context: Context,
            targetHour: Int = DEFAULT_HOUR_EXEC,
            targetMinute: Int = DEFAULT_MINUTE_EXEC
        ) {
            val delay = DateTimeUtils.calculateTimeUntilNext(targetHour, targetMinute)
            Log.d("DailyRecommendWorker", "Scheduling work with $delay ms at $targetHour:$targetMinute.")

            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()

            val request = PeriodicWorkRequestBuilder<DailyRecommendWorker>(1, TimeUnit.DAYS)
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

private const val DEFAULT_HOUR_EXEC = 7
private const val DEFAULT_MINUTE_EXEC = 0
