package com.youllbecold.recomendation.internal.model

import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings

/**
 * Helper class for different body parts/areas of clothing.
 */
internal enum class BodyPart {
    HEAD,
    NECK,
    TOP,
    BOTTOM,
    HANDS,
    FEET;

    /**
     * Returns true if the body part requires only single item of clothing.
     */
    fun isSingleItem(): Boolean = this !in listOf(TOP, BOTTOM)

    /**
     * Returns the feeling for this body part.
     *
     * @param feelings The feelings.
     */
    fun getFeeling(feelings: Feelings): Feeling = when(this) {
        HEAD -> feelings.head
        NECK -> feelings.neck
        TOP -> feelings.top
        BOTTOM -> feelings.bottom
        HANDS -> feelings.hand
        FEET -> feelings.feet
    }
}
