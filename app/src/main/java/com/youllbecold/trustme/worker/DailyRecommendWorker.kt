package com.youllbecold.trustme.worker

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
import com.youllbecold.trustme.notifications.DailyRecommendNotification
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.trustme.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.PermissionHelper
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

    private val dataStorePreferences: DataStorePreferences by inject()

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        if (!PermissionHelper.hasNotificationPermission(appContext) ||
            !PermissionHelper.hasLocationPermission(appContext)) {
            Log.d("DailyRecommendWorker", "Some permissions are missing, aborting")

            // Disable notification and cancel this worker
            dataStorePreferences.setAllowRecommendNotification(false)
            cancel(appContext)

            return Result.success()
        }

        val geoLocation = LocationHelper.getLastLocation(appContext)

        if (geoLocation == null) {
            Log.d("DailyRecommendWorker", "Location is null, aborting")
            return Result.success()
        }

        val weather = currentWeatherUseCase.getCurrentWeather(geoLocation)

        if (weather == null) {
            Log.d("DailyRecommendWorker", "Weather is null, aborting")
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
            val delay = WorkerUtils.calculateTimeUntilNext(targetHour, targetMinute)
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
