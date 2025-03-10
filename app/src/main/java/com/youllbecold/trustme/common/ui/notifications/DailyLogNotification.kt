package com.youllbecold.trustme.common.ui.notifications

import android.app.Application
import android.app.NotificationManager
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.notifications.channel.ChannelId
import com.youllbecold.trustme.common.ui.utils.IntentUtils
import com.youllbecold.trustme.common.ui.notifications.utils.NotificationUtils

/**
 * Class for showing a notification to remind the user to log their feeling.
 */
class DailyLogNotification(
    private val app: Application,
    private val notificationManager: NotificationManager
) {
    /**
     * Show a notification to remind the user to log their feeling.
     */
    fun show() {
        val notification = NotificationUtils.createNotification(
            context = app,
            title = app.getString(R.string.notif_daily_log_title),
            description = app.getString(R.string.notif_daily_log_desc),
            smallIcon = R.drawable.ic_snowflake,
            channelId = ChannelId.DAILY,
            intent = IntentUtils.createMainPendingIntent(app)
        )

        notificationManager.notify(
            DAILY_LOG_NOTIF_ID,
            notification
        )
    }
}

private const val DAILY_LOG_NOTIF_ID = 1
