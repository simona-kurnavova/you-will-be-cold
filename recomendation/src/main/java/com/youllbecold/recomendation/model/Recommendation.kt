package com.youllbecold.recomendation.model

import com.youllbecold.logdatabase.model.ClothesModel

/**
 * Data class for recommendation.
 *
 * @param clothes List of clothes to wear.
 * @param certainty Certainty level of this recommendation.
 * @param uvLevel UV protection recommendation.
 * @param rainLevel Rain level recommendation.
 */
data class Recommendation(
    val clothes: List<ClothesModel> = emptyList(),
    val certainty: Certainty = Certainty.Low,
    val uvLevel: UvRecommendation,
    val rainLevel: RainRecommendation
)
