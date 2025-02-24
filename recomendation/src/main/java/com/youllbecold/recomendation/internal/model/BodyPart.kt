package com.youllbecold.recomendation.internal.model

import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings

internal enum class BodyPart {
    HEAD,
    NECK,
    TOP,
    BOTTOM,
    HANDS,
    FEET;

    fun isSingleItem(): Boolean = this !in listOf(TOP, BOTTOM)

    fun getFeeling(feelings: Feelings): Feeling = when(this) {
        HEAD -> feelings.head
        NECK -> feelings.neck
        TOP -> feelings.top
        BOTTOM -> feelings.bottom
        HANDS -> feelings.hand
        FEET -> feelings.feet
    }
}
