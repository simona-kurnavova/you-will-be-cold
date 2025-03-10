package com.youllbecold.trustme.recommend.home.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.ui.components.themed.IconType

/**
 * The hourly temperature forecast for next few hours.
 */
@Stable
data class HourlyTemperature(
    val formattedTime: String,
    val temperature: Int,
    val weatherIcon: IconType
)
