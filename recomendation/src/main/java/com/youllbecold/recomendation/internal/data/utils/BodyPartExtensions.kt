package com.youllbecold.recomendation.internal.data.utils

import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.recomendation.internal.data.outfit.BodyPart
import com.youllbecold.recomendation.internal.data.outfit.ClothesWeight

/**
 * Returns the feeling for this body part.
 *
 * @param feelings The feelings.
 */
internal fun BodyPart.getFeeling(feelings: Feelings): Feeling = when (this) {
    BodyPart.HEAD -> feelings.head
    BodyPart.NECK -> feelings.neck
    BodyPart.TOP -> feelings.top
    BodyPart.BOTTOM -> feelings.bottom
    BodyPart.HANDS -> feelings.hand
    BodyPart.FEET -> feelings.feet
}

/**
 * Returns the item selector for this body part.
 */
internal fun BodyPart.getItemSelector() = when (this) {
    BodyPart.HEAD -> { weight: ClothesWeight -> weight.head }
    BodyPart.NECK -> { weight: ClothesWeight -> weight.neck }
    BodyPart.TOP -> { weight: ClothesWeight -> weight.top }
    BodyPart.BOTTOM -> { weight: ClothesWeight -> weight.bottom }
    BodyPart.HANDS -> { weight: ClothesWeight -> weight.hands }
    BodyPart.FEET -> { weight: ClothesWeight -> weight.feet }
}
