package com.youllbecold.trustme.ui.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.icontext.ClickableText
import com.youllbecold.trustme.ui.components.generic.icontext.IconText
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.components.utils.formatDate
import com.youllbecold.trustme.ui.components.utils.formatTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.utils.getTemperatureString
import com.youllbecold.trustme.ui.utils.getTitle
import com.youllbecold.trustme.ui.utils.icon
import com.youllbecold.trustme.ui.utils.labeled
import com.youllbecold.trustme.ui.utils.worstFeeling
import com.youllbecold.trustme.ui.viewmodels.FeelingsState
import com.youllbecold.trustme.ui.viewmodels.LogState
import com.youllbecold.trustme.ui.viewmodels.WeatherState
import kotlinx.collections.immutable.persistentSetOf
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun LogCard(
    log: LogState,
    editAction: () -> Unit,
    deleteAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier.clickable(onClick = { expanded = !expanded }),
    ) {
        if (expanded) {
            ExpandedCardContent(
                log = log,
                editAction = editAction,
                deleteAction = deleteAction
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val worstFeeling = log.feelings.worstFeeling()

                NonExpandedCardContent(
                    title = stringResource(
                        R.string.log_item_label,
                        worstFeeling.getTitle()
                    ),
                    iconType = worstFeeling.icon,
                    date = log.date,
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.expand_card_action),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .clickable(onClick = { expanded = !expanded })
                        .padding(INSIDE_PADDING.dp)
                )
            }
        }
    }
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
            text = worstFeeling.getTitle(),
            iconType = worstFeeling.icon
        )

        val dateTime = "${log.date.date.formatDate()} ${log.timeFrom.time.formatTime()} - ${log.timeTo.time.formatTime()}"

        Text(
            text = dateTime,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = DATE_ALPHA)
        )

        log.weather?.let {
            val temperatureWithUnits = LocalContext.current.getTemperatureString(it.avgTemperature, true)
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
                        text = it.getTitle(),
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
                    text = "$label ${feeling.getTitle()}",
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
    date: ImmutableDate,
    modifier: Modifier = Modifier
) {
    IconText(
        text = title,
        subtitle = date.date.formatDate(),
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
                date = ImmutableDate(LocalDate.now()),
                timeFrom = ImmutableTime(time),
                timeTo = ImmutableTime(time),
                feelings = FeelingsState(),
                clothes = persistentSetOf(
                    Clothes.SHORT_SLEEVE, Clothes.TENNIS_SHOES
                ),
                weather = WeatherState(
                    avgTemperature = 20.0,
                    apparentTemperatureMin = 15.0,
                    apparentTemperatureMax = 25.0
                )
            ),
            editAction = {},
            deleteAction = {}
        )
    }
}
