package com.youllbecold.recomendation.internal.data.outfit

/**
 * Class for different body parts/areas of clothing.
 */
internal enum class BodyPart {
    HEAD,
    NECK,
    TOP,
    BOTTOM,
    HANDS,
    FEET;

    /**
     * Returns true if the body part requires/can have only single item of clothing.
     */
    fun isSingleItem(): Boolean = this !in listOf(TOP, BOTTOM)
}
