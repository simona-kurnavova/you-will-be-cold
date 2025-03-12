package com.youllbecold.trustme.log.ui.model

import androidx.annotation.StringRes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.IconType

/**
 * Represents the state of a feeling for one bodypart.
 */
enum class FeelingState {
    VERY_COLD,
    COLD,
    NORMAL,
    WARM,
    VERY_WARM,
}

/**
 * Returns the title associated with the feeling state.
 */
@get:StringRes
val  FeelingState.feelingName: Int
    get() = when (this) {
        FeelingState.VERY_WARM -> R.string.feeling_very_hot
        FeelingState.WARM -> R.string.feeling_hot
        FeelingState.NORMAL -> R.string.feeling_normal
        FeelingState.COLD -> R.string.feeling_cold
        FeelingState.VERY_COLD -> R.string.feeling_very_cold
    }

/**
 * Returns the icon associated with the feeling state.
 */
val FeelingState.icon: IconType
    get() = when (this) {
        FeelingState.VERY_WARM -> IconType.Fire
        FeelingState.WARM -> IconType.Sun
        FeelingState.NORMAL -> IconType.SmileEmoji
        FeelingState.COLD -> IconType.Snowflake
        FeelingState.VERY_COLD -> IconType.Popsicle
    }
