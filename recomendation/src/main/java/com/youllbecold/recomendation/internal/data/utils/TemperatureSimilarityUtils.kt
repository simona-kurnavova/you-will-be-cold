package com.youllbecold.recomendation.internal.data.utils

/**
 * Utility functions for similarity calculations.
 *
 * Similarity = how close the temperatures are to each other.
 */
internal object TemperatureSimilarityUtils {
    /**
     * Returns the similarity factor between two ranges.
     *
     * @param tempFrom The start of the first range.
     * @param tempTo The end of the first range.
     * @param newTempFrom The start of the second range.
     * @param newTempTo The end of the second range.
     * @return The similarity factor.
     */
    fun calculateRangeSimilarity(tempFrom: Double, tempTo: Double, newTempFrom: Double, newTempTo: Double): Double {
        if (tempFrom > tempTo || newTempFrom > newTempTo) return 0.0 // Invalid range check

        val overlapStart = maxOf(tempFrom, newTempFrom)
        val overlapEnd = minOf(tempTo, newTempTo)
        val overlapLength = maxOf(0.0, overlapEnd - overlapStart)

        val unionLength = tempTo - tempFrom
        if (unionLength == 0.0) return 0.0 // Prevent division by zero

        return overlapLength / unionLength
    }
}
