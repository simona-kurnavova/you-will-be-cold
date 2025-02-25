package com.youllbecold.trustme.worker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.youllbecold.trustme.notifications.NotificationHelper
import com.youllbecold.trustme.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.PermissionHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DailyRecommendWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams), KoinComponent {

    private val currentWeatherUseCase: CurrentWeatherUseCase by inject()

    private val notificationHelper: NotificationHelper by inject()

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        if (!PermissionHelper.hasNotificationPermission(appContext) ||
            !PermissionHelper.hasLocationPermission(appContext)) {
            Log.d("DailyRecommendWorker", "Some permissions are missing, aborting")
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

        notificationHelper.showRecommendNotification(
            temperature = weather.temperature,
            weatherEvaluation = weather.weatherEvaluation
        )

        return Result.success()
    }

    companion object {
        const val WORK_NAME = "DailyRecommendWorker"

        fun schedule(context: Context, targetHour: Int = 7, targetMinute: Int = 0) {
            val delay = WorkerUtils.calculateTimeUntilNext(targetHour, targetMinute)

            Log.d("DailyRecommendWorker", "Scheduling work in $delay minutes at $targetHour:$targetMinute daily")

            val request = WorkerUtils.createDailyWorkRequest(delay)

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