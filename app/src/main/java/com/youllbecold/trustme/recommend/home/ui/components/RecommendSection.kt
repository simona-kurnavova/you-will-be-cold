package com.youllbecold.trustme.recommend.home.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.recommend.ranged.ui.components.RecommendationCard
import com.youllbecold.trustme.common.ui.components.inputs.ChipSelectCard
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.recommend.home.ui.model.Forecast
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun RecommendSection(
    weather: Forecast,
    horizontalPadding: Int,
    modifier: Modifier = Modifier,
) {
    var selectedOption by remember { mutableIntStateOf(RecommendationChip.NOW.ordinal) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ThemedText(
            text = stringResource(R.string.home_section_recommendations),
            modifier = Modifier.padding(vertical = PADDING_BETWEEN_ITEMS.dp, horizontal = horizontalPadding.dp)
        )

        ChipSelectCard(
            options = getRecomOptions(),
            onOptionSelected = { selectedOption = it },
            selectedOption = selectedOption,
            modifier = Modifier.padding(bottom = PADDING_BETWEEN_ITEMS.dp),
            horizontalPadding = horizontalPadding,
        ) { page ->
            val weather = when (RecommendationChip.entries[page]) {
                RecommendationChip.NOW -> weather.current
                RecommendationChip.TODAY -> weather.today
                RecommendationChip.TOMORROW -> weather.tomorrow
            }

            weather.recommendationState?.let {
                RecommendationCard(
                    weather = weather,
                    clothes = it.clothes,
                )
            }
        }
    }
}

private enum class RecommendationChip(@StringRes val stringId: Int) {
    NOW(R.string.recommendation_chip_now),
    TODAY(R.string.recommendation_chip_today),
    TOMORROW(R.string.recommendation_chip_tomorrow);
}

@Composable
private fun getRecomOptions(): PersistentList<String> =
    RecommendationChip.entries
        .map { stringResource(it.stringId) }
        .toPersistentList()

private const val PADDING_BETWEEN_ITEMS = 8
