package com.youllbecold.recomendation.internal.data.utils

import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.recomendation.internal.data.outfit.BodyPart

/**
 * Utility class for certainty calculations.
 *
 * Certainty = certainty of log based on feeling.
 */
internal object LogCertaintyUtils {
    /**
     * Calculates certainty of clothes based on feelings.
     */
    fun calculateCertaintyMap(feelings: Feelings): Map<BodyPart, Double> = mapOf(
        BodyPart.HEAD to evaluateCertainty(feelings.head, BodyPart.HEAD.isSingleItem()),
        BodyPart.NECK to evaluateCertainty(feelings.neck, BodyPart.NECK.isSingleItem()),
        BodyPart.TOP to evaluateCertainty(feelings.top, BodyPart.TOP.isSingleItem()),
        BodyPart.BOTTOM to evaluateCertainty(feelings.bottom, BodyPart.BOTTOM.isSingleItem()),
        BodyPart.HANDS to evaluateCertainty(feelings.hand, BodyPart.HANDS.isSingleItem()),
        BodyPart.FEET to evaluateCertainty(feelings.feet, BodyPart.FEET.isSingleItem()),
    )

    private fun evaluateCertainty(
        feeling: Feeling,
        isSingleItem: Boolean
    ): Double = when(feeling) {
        Feeling.NORMAL -> 1.0
        Feeling.COLD,
        Feeling.WARM -> if (isSingleItem) 0.9 else 0.8
        Feeling.VERY_COLD,
        Feeling.VERY_WARM -> if (isSingleItem) 0.6 else 0.5
    }
}
