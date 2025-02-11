package com.youllbecold.recomendation.internal

import com.youllbecold.logdatabase.model.Clothes

internal object OutfitHelper {
    private val clothesWeights = mapOf(
        Clothes.SHORT_SLEEVE to ClothesWeight(top = 2),
        Clothes.LONG_SLEEVE to ClothesWeight(top = 3),
        Clothes.SHORT_SKIRT to ClothesWeight(bottom = 1),
        Clothes.SHORTS to ClothesWeight(bottom = 1),
        Clothes.JEANS to ClothesWeight(bottom = 3),
        Clothes.SANDALS to ClothesWeight(feet = 2),
        Clothes.TENNIS_SHOES to ClothesWeight(feet = 3),
        Clothes.DRESS to ClothesWeight(top = 2, bottom = 2)
    )

    fun calculateWeight(outfit: List<Clothes>): ClothesWeight = outfit
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

internal class ClothesWeight(
    val head: Int = 0,
    val neck: Int = 0,
    val top: Int = 0,
    val bottom: Int = 0,
    val hands: Int = 0,
    val feet: Int = 0,
)
