package com.youllbecold.trustme.log.history.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.cards.ExpandableCard
import com.youllbecold.trustme.common.ui.components.icontext.ClickableText
import com.youllbecold.trustme.common.ui.components.icontext.IconText
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.components.utils.DateState
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.components.utils.TimeState
import com.youllbecold.trustme.common.ui.components.utils.formatDate
import com.youllbecold.trustme.common.ui.components.utils.formatTime
import com.youllbecold.trustme.log.ui.model.FeelingsState
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.WeatherParams
import com.youllbecold.trustme.common.ui.model.clothes.mappers.clothesName
import com.youllbecold.trustme.common.ui.model.clothes.mappers.icon
import com.youllbecold.trustme.log.ui.model.mappers.labeled
import com.youllbecold.trustme.log.ui.model.mappers.worstFeeling
import com.youllbecold.trustme.recommend.usecases.model.mappers.getTemperatureString
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.log.ui.model.mappers.clothesName
import com.youllbecold.trustme.log.ui.model.mappers.icon
import kotlinx.collections.immutable.persistentSetOf
import java.time.LocalTime

@Composable
fun LogCard(
    log: LogState,
    editAction: () -> Unit,
    deleteAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val worstFeeling = log.feelings.worstFeeling()

    ExpandableCard(
        nonExpandedContent = {
            NonExpandedCardContent(
                title = stringResource(
                    R.string.log_item_label,
                    worstFeeling.clothesName()
                ),
                iconType = worstFeeling.icon,
                formattedDate = log.date.formatDate(),
            )
        },
        expandedContent = {
            ExpandedCardContent(
                log = log,
                editAction = editAction,
                deleteAction = deleteAction
            )
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedCardContent(
    log: LogState,
    editAction: () -> Unit,
    deleteAction: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(INSIDE_PADDING.dp),
        modifier = Modifier.padding(SPACE_BETWEEN_ITEMS.dp)
    ) {
        val worstFeeling = log.feelings.worstFeeling()
        IconText(
            text = worstFeeling.clothesName(),
            iconType = worstFeeling.icon
        )

        val dateTime = "${log.date.formatDate()} ${log.timeFrom.formatTime()} - ${log.timeTo.formatTime()}"

        Text(
            text = dateTime,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = DATE_ALPHA)
        )

        log.weather?.let {
            val temperatureWithUnits = LocalContext.current.getTemperatureString(it.avgTemperature, it.useCelsiusUnits)
            val weatherInfo = stringResource(R.string.log_detail_weather_info, temperatureWithUnits)

            Text(
                text = weatherInfo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = DATE_ALPHA)
            )
        }

        if (log.clothes.isNotEmpty()) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground,
                thickness = DIVIDER_THICKNESS.dp
            )

            Column{
                log.clothes.forEach {
                    IconText(
                        text = it.clothesName(),
                        iconType = it.icon,
                        modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
                    )
                }
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground,
            thickness = DIVIDER_THICKNESS.dp
        )

        Column {
            log.feelings.labeled().forEach { (label, feeling) ->
                IconText(
                    text = "$label ${feeling.clothesName()}",
                    iconType = feeling.icon,
                    modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            ClickableText(
                text = stringResource(R.string.action_edit),
                iconType = IconType.Pencil,
                onClick = editAction,
            )

            Spacer(modifier = Modifier.width(SPACE_BETWEEN_ACTIONS.dp))

            ClickableText(
                text = stringResource(R.string.action_delete),
                iconType = IconType.Trash,
                onClick = deleteAction,
            )
        }
    }
}

private const val SPACE_BETWEEN_ACTIONS = 8
private const val SPACE_BETWEEN_ITEMS = 16
private const val DATE_ALPHA = 0.6f
private const val ITEMS_PADDING = 8
private const val DIVIDER_THICKNESS = 0.1f

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
                feelings = FeelingsState(),
                clothes = persistentSetOf(
                    Clothes.SHORT_SLEEVE, Clothes.TENNIS_SHOES
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
