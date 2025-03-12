package com.youllbecold.trustme.common.ui.notifications

import android.app.Application
import android.app.NotificationManager
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.notifications.channel.ChannelId
import com.youllbecold.trustme.common.ui.utils.IntentUtils
import com.youllbecold.trustme.common.ui.notifications.utils.NotificationUtils
import com.youllbecold.trustme.recommend.ui.mappers.icon
import com.youllbecold.weather.model.WeatherEvaluation

/**
 * Class for showing daily recommendation notifications.
 */
class DailyRecommendNotification(
    private val app: Application,
    private val notificationManager: NotificationManager
) {
    /**
     * Show a notification with a recommendation based on the current weather.
     */
    fun show(temperature: Double, useCelsiusUnits: Boolean, weatherEvaluation: WeatherEvaluation) {
        val temperatureWithUnits = if (useCelsiusUnits) {
            app.getString(R.string.temperature_celsius, temperature)
        } else {
            app.getString(R.string.temperature_fahrenheit, temperature)
        }

        val notification = NotificationUtils.createNotification(
            context = app,
            title =  app.getString(R.string.notif_recommend_title),
            description = app.getString(R.string.notif_recommend_desc, temperatureWithUnits),
            smallIcon = weatherEvaluation.icon.resource,
            channelId = ChannelId.DAILY,
            intent = IntentUtils.createMainPendingIntent(app)
        )

        notificationManager.notify(
            DAILY_RECOMMEND_NOTIF_ID,
            notification
        )
    }
}

private const val DAILY_RECOMMEND_NOTIF_ID = 2
