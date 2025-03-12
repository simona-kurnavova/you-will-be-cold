package com.youllbecold.trustme.log.history.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.logdatabase.model.ClothesModel
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.cards.ExpandableCard
import com.youllbecold.trustme.common.ui.components.icontext.IconText
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.components.utils.DateState
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.components.utils.TimeState
import com.youllbecold.trustme.common.ui.components.utils.formatDate
import com.youllbecold.trustme.common.ui.mappers.toClothes
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.WeatherParams
import com.youllbecold.trustme.log.ui.model.feelingName
import com.youllbecold.trustme.log.ui.model.icon
import com.youllbecold.trustme.log.ui.model.worstFeeling
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import java.time.LocalTime

@Composable
fun LogCard(
    log: LogState,
    editAction: () -> Unit,
    deleteAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val worstFeeling = log.feelings.worstFeeling

    ExpandableCard(
        nonExpandedContent = {
            NonExpandedCardContent(
                title = stringResource(
                    R.string.log_item_label,
                    stringResource(worstFeeling.feelingName)
                ),
                iconType = worstFeeling.icon,
                formattedDate = log.date.formatDate(),
            )
        },
        expandedContent = {
            ExpandedLogCardContent(
                log = log,
                editAction = editAction,
                deleteAction = deleteAction
            )
        },
        modifier = modifier
    )
}

@Composable
private fun NonExpandedCardContent(
    title: String,
    iconType: IconType,
    formattedDate: String,
    modifier: Modifier = Modifier
) {
    IconText(
        text = title,
        subtitle = formattedDate,
        iconType = iconType,
        modifier = modifier.padding(INSIDE_PADDING.dp)
    )
}

private const val INSIDE_PADDING = 16

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun LogCardPreview() {
    YoullBeColdTheme {
        val time = LocalTime.now()
        LogCard(
            log = LogState(
                dateTimeState = DateTimeState(
                    date = DateState(2022, 1, 1),
                    timeFrom = TimeState(time.hour, time.minute),
                    timeTo = TimeState(time.hour + 1, time.minute)
                ),
                feelings = persistentListOf(),
                clothes = persistentSetOf(
                    ClothesModel.SHORT_SLEEVE.toClothes(),
                    ClothesModel.TENNIS_SHOES.toClothes(),
                ),
                weather = WeatherParams(
                    avgTemperature = 20.0,
                    apparentTemperatureMin = 15.0,
                    apparentTemperatureMax = 25.0,
                    useCelsiusUnits = true
                )
            ),
            editAction = {},
            deleteAction = {}
        )
    }
}
