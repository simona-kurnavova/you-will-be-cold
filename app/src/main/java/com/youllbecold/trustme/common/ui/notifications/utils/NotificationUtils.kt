package com.youllbecold.trustme.common.ui.notifications.utils

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat

/**
 * Helper class for showing notifications and setting up notification channels.
 */
object NotificationUtils {
    /**
     * Create a notification.
     *
     * @param context The context to use.
     * @param title The title of the notification.
     * @param description The description of the notification.
     * @param smallIcon The small icon to use.
     * @param intent The intent to use when the notification is clicked.
     * @param channelId The channel ID to use.
     *
     * @return The created notification.
     */
    fun createNotification(
        context: Context,
        title: String,
        description: String,
        @DrawableRes smallIcon: Int,
        intent: PendingIntent,
        channelId: String,
    ): Notification = NotificationCompat.Builder(context, channelId)
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
        .build()
}