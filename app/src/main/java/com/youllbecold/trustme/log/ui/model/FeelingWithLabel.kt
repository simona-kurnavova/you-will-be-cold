package com.youllbecold.trustme.log.ui.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.trustme.log.ui.mappers.toFeelingsWithLabel
import kotlinx.collections.immutable.PersistentList

/**
 * Feeling with label.
 */
@Stable
data class FeelingWithLabel(
    val bodyPart: BodyPart,
    val feeling: FeelingState,
    @StringRes
    val label: Int,
)

/**
 * Represents bodu parts.
 */
enum class BodyPart {
    HEAD,
    NECK,
    TOP,
    BOTTOM,
    HANDS,
    FEET
}

