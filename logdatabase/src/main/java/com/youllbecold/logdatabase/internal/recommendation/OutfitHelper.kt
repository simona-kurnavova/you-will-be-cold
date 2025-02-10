package com.youllbecold.logdatabase.internal.recommendation

import com.youllbecold.logdatabase.internal.log.entity.ClothesId

internal object OutfitHelper {
    fun calculateWeight(outfit: List<ClothesId>): ClothesWeight = outfit
        .mapNotNull { clothesWeights[it] }
        .let { concatWeights(it) }

    private fun concatWeights(weights: List<ClothesWeight>): ClothesWeight =
        ClothesWeight(
            head = weights.sumOf { it.head },
            neck = weights.sumOf { it.neck },
            top = weights.sumOf { it.top },
            bottom = weights.sumOf { it.bottom },
            hands = weights.sumOf { it.hands },
            feet = weights.sumOf { it.feet }
        )
}

private val clothesWeights = mapOf(
    ClothesId.SHORT_SLEEVE to ClothesWeight(top = 2),
    ClothesId.LONG_SLEEVE to ClothesWeight(top = 3),
    ClothesId.SHORT_SKIRT to ClothesWeight(bottom = 1),
    ClothesId.SHORTS to ClothesWeight(bottom = 1),
    ClothesId.JEANS to ClothesWeight(bottom = 3),
    ClothesId.SANDALS to ClothesWeight(feet = 2),
    ClothesId.TENNIS_SHOES to ClothesWeight(feet = 3),
    ClothesId.DRESS to ClothesWeight(top = 2, bottom = 2)
)

internal class ClothesWeight(
    val head: Int = 0,
    val neck: Int = 0,
    val top: Int = 0,
    val bottom: Int = 0,
    val hands: Int = 0,
    val feet: Int = 0,
)
