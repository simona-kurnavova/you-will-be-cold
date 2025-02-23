package com.youllbecold.trustme.notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.youllbecold.trustme.MainActivity
import com.youllbecold.trustme.R

class NotificationHelper(
    private val app: Application,
) {
    private val notificationManager: NotificationManager by lazy {
        app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        createNotificationChannel()
    }

    fun showDailyLogNotification() {
        val title = app.getString(R.string.notif_daily_log_title)
        val description = app.getString(R.string.notif_daily_log_desc)

        var builder = NotificationCompat.Builder(app, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_snowflake)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createMainIntent())

        notificationManager.notify(
            DAILY_LOG_NOTIFICATION_ID,
            builder.build()
        )
    }

    private fun createNotificationChannel() {
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