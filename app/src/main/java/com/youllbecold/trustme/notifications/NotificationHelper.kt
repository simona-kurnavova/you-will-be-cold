package com.youllbecold.trustme.notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.youllbecold.trustme.MainActivity
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.utils.icon
import com.youllbecold.trustme.ui.viewmodels.SettingsAction
import com.youllbecold.weather.model.WeatherEvaluation
import org.koin.core.annotation.Singleton

/**
 * Helper class for showing notifications and setting up notification channels.
 */
@Singleton
internal class NotificationHelper(
    private val app: Application,
) {
    private val notificationManager: NotificationManager by lazy {
        app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        createDailyNotificationChannel()
    }

    /**
     * Show a notification to remind the user to log their feeling.
     */
    fun showDailyLogNotification() {
        createAndShowNotification(
            title = app.getString(R.string.notif_daily_log_title),
            description = app.getString(R.string.notif_daily_log_desc),
            notificationId = DAILY_LOG_NOTIFICATION_ID,
            smallIcon = R.drawable.ic_snowflake
        )
    }

    /**
     * Show a notification with a recommendation based on the current weather.
     */
    fun showRecommendNotification(temperature: Double, useCelsiusUnits: Boolean, weatherEvaluation: WeatherEvaluation) {
        val temperatureWithUnits = if (useCelsiusUnits) {
            app.getString(R.string.temperature_celsius, temperature)
        } else {
            app.getString(R.string.temperature_fahrenheit, temperature)
        }

        createAndShowNotification(
            title =  app.getString(R.string.notif_recommend_title),
            description = app.getString(R.string.notif_recommend_desc, temperatureWithUnits),
            notificationId = DAILY_RECOMMEND_NOTIFICATION_ID,
            smallIcon = weatherEvaluation.icon.resource
        )
    }

    private fun createAndShowNotification(
        title: String,
        description: String,
        notificationId: Int,
        @DrawableRes smallIcon: Int,
        intent: PendingIntent = createMainIntent(),
        channelId: String = CHANNEL_ID,
    ) {
        var builder = NotificationCompat.Builder(app, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(intent)

        notificationManager.notify(
            notificationId,
            builder.build()
        )
    }

    private fun createDailyNotificationChannel() {
        val name = app.getString(R.string.daily_notification_channel_name)
        val descriptionText = app.getString(R.string.daily_notification_channel_descr)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun createMainIntent(): PendingIntent {
        val intent = Intent(app, MainActivity::class.java)
        return PendingIntent.getActivity(app, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
}

private const val CHANNEL_ID = "daily_reminders_channel"

private const val DAILY_LOG_NOTIFICATION_ID = 1
private const val DAILY_RECOMMEND_NOTIFICATION_ID = 2
