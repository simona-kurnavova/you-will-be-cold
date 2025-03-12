package com.youllbecold.trustme.log.ui.model.mappers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.components.inputs.SelectableItemContent
import com.youllbecold.trustme.log.ui.model.FeelingState
import com.youllbecold.trustme.log.ui.model.FeelingsState

/**
 * Returns the title associated with the feeling state.
 */
@Composable
fun FeelingState.clothesName(): String = when (this) {
    FeelingState.VERY_WARM -> stringResource(id = R.string.feeling_very_hot)
    FeelingState.WARM -> stringResource(id = R.string.feeling_hot)
    FeelingState.NORMAL -> stringResource(id = R.string.feeling_normal)
    FeelingState.COLD -> stringResource(id = R.string.feeling_cold)
    FeelingState.VERY_COLD -> stringResource(id = R.string.feeling_very_cold)
}

/**
 * Returns the icon associated with the feeling state.
 */
val FeelingState.icon: IconType
    get() = when (this) {
        FeelingState.VERY_WARM -> IconType.Fire
        FeelingState.WARM -> IconType.Sun
        FeelingState.NORMAL -> IconType.SmileEmoji
        FeelingState.COLD -> IconType.Snowflake
        FeelingState.VERY_COLD -> IconType.Popsicle
    }

/**
 * Returns a list of [FeelingWithLabel] for the feelings state.
 */
@Composable
fun FeelingsState.getFeelingWithLabel(onChange: (FeelingsState) -> Unit): List<FeelingWithLabel> =
    listOf(
        FeelingWithLabel(
            neck,
            stringResource(R.string.label_neck),
            { onChange(this.copy(neck = it)) }),
        FeelingWithLabel(
            head,
            stringResource(R.string.label_head),
            { onChange(this.copy(head = it)) }),
        FeelingWithLabel(
            top,
            stringResource(R.string.label_top),
            { onChange(this.copy(top = it)) }),
        FeelingWithLabel(
            bottom,
            stringResource(R.string.label_bottom),
            { onChange(this.copy(bottom = it)) }),
        FeelingWithLabel(
            feet,
            stringResource(R.string.label_feet),
            { onChange(this.copy(feet = it)) }),
        FeelingWithLabel(
            hand,
            stringResource(R.string.label_hands),
            { onChange(this.copy(hand = it)) },
        )
    )

data class FeelingWithLabel(
    val feeling: FeelingState,
    val label: String,
    val update: (FeelingState) -> Unit
)


/**
 * Returns a list of pairs of strings representing the feelings state.
 */
@Composable
fun FeelingsState.labeled(): List<Pair<String, FeelingState>> = listOf(
    stringResource(R.string.label_neck) to neck,
    stringResource(R.string.label_head) to head,
    stringResource(R.string.label_top) to top,
    stringResource(R.string.label_bottom) to bottom,
    stringResource(R.string.label_feet) to feet,
    stringResource(R.string.label_hands) to hand
)

@Composable
fun FeelingsState.worstFeeling(): FeelingState = labeled()
    .filter { it.second != FeelingState.NORMAL }
    .minByOrNull { it.second.ordinal }?.second // Consider cold worst than hot.
    ?: FeelingState.NORMAL
