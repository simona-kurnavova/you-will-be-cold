package com.youllbecold.trustme.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.SelectableItemContent
import com.youllbecold.trustme.ui.viewmodels.FeelingState
import com.youllbecold.trustme.ui.viewmodels.FeelingsState

@Composable
fun List<FeelingState>.toSelectableItemContent(): List<SelectableItemContent> = map { feeling ->
    when (feeling) {
        FeelingState.VERY_WARM -> SelectableItemContent(
            IconType.Fire,
            stringResource(id = R.string.feeling_very_hot)
        )

        FeelingState.WARM -> SelectableItemContent(
            IconType.Sun,
            stringResource(id = R.string.feeling_hot)
        )

        FeelingState.NORMAL -> SelectableItemContent(
            IconType.SmileEmoji,
            stringResource(id = R.string.feeling_normal)
        )

        FeelingState.COLD -> SelectableItemContent(
            IconType.Snowflake,
            stringResource(id = R.string.feeling_cold)
        )

        FeelingState.VERY_COLD -> SelectableItemContent(
            IconType.Popsicle,
            stringResource(id = R.string.feeling_very_cold)
        )
    }
}

data class FeelingWithLabel(
    val feeling: FeelingState,
    val label: String,
    val update: (FeelingState) -> Unit
)

fun FeelingsState.getFeelingList(onChange: (FeelingsState) -> Unit): List<FeelingWithLabel> =
    listOf(
        FeelingWithLabel(
            neck,
            "Neck:",
            { onChange(this.copy(neck = it)) }),
        FeelingWithLabel(
            head,
            "Head:",
            { onChange(this.copy(head = it)) }),
        FeelingWithLabel(
            top,
            "Top:",
            { onChange(this.copy(top = it)) }),
        FeelingWithLabel(
            bottom,
            "Bottom:",
            { onChange(this.copy(bottom = it)) }),
        FeelingWithLabel(
            feet,
            "Feet:",
            { onChange(this.copy(feet = it)) }),
        FeelingWithLabel(
            hand, "Hands:", { onChange(this.copy(hand = it)) },
        )
    )