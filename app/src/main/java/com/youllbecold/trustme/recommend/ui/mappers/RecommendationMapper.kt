package com.youllbecold.trustme.recommend.ui.mappers

import android.content.Context
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.Recommendation
import com.youllbecold.recomendation.model.UvRecommendation
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.mappers.toClothes
import com.youllbecold.trustme.recommend.ui.model.RecommendationState
import kotlinx.collections.immutable.toPersistentList

/**
 * Map a [Recommendation] to a [RecommendationState].
 */
fun Recommendation.toRecommendationState(context: Context): RecommendationState =
    RecommendationState(
        uvWarning = uvLevel.getUvWarning(context),
        rainWarning = rainLevel.getRainWarning(context),
        clothes = clothes.map { it.toClothes() }.toPersistentList(),
        certaintyLevel = certainty.getTitle(context)
    )

/**
 * Get the title for the [UvRecommendation].
 */
private fun UvRecommendation.getUvWarning(context: Context): String? = when (this) {
    UvRecommendation.NoProtection ->  null
    UvRecommendation.LowProtection -> R.string.uv_recom_low
    UvRecommendation.MediumProtection -> R.string.uv_recom_medium
    UvRecommendation.HighProtection -> R.string.uv_recom_high
}?.let { context.getString(it) }

/**
 * Get the title for the [RainRecommendation].
 */
private fun RainRecommendation.getRainWarning(context: Context): String? = when (this) {
    RainRecommendation.NoRain -> null
    RainRecommendation.LightRain -> R.string.rain_recom_low
    RainRecommendation.MediumRain -> R.string.rain_recom_medium
    RainRecommendation.HeavyRain -> R.string.rain_recom_high
}?.let { context.getString(it) }

/**
 * Get the title for the [Certainty].
 */
private fun Certainty.getTitle(context: Context): String = when (this) {
    Certainty.Low -> R.string.certainity_low
    Certainty.Medium -> R.string.certainity_medium
    Certainty.High -> R.string.certainity_high
}.let { context.getString(it) }
