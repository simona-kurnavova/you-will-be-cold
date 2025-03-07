package com.youllbecold.trustme.notifications.channel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.youllbecold.trustme.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Initializes notification channels.
 */
object ChannelInitializer : KoinComponent {
    /**
     * Create all needed notification channels.
     */
    fun initChannels(context: Context) {
        val notificationManager = get<NotificationManager>()
        notificationManager.createDailyNotificationChannel(context)
    }

    /**
     * Create a notification channel for daily reminders.
     */
    private fun NotificationManager.createDailyNotificationChannel(context: Context) {
        val name = context.getString(R.string.daily_notification_channel_name)
        val descriptionText = context.getString(R.string.daily_notification_channel_descr)
        val channel = NotificationChannel(ChannelId.DAILY, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = descriptionText
        }

        createNotificationChannel(channel)
    }
}
