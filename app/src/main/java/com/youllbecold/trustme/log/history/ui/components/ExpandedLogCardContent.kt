package com.youllbecold.trustme.log.history.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.logdatabase.model.ClothesModel
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.icontext.ClickableText
import com.youllbecold.trustme.common.ui.components.icontext.IconText
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.components.themed.ThemedHorizontalDivider
import com.youllbecold.trustme.common.ui.components.utils.DateState
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.components.utils.TimeState
import com.youllbecold.trustme.common.ui.components.utils.formatDate
import com.youllbecold.trustme.common.ui.components.utils.formatTime
import com.youllbecold.trustme.common.ui.mappers.toClothes
import com.youllbecold.trustme.common.ui.model.clothes.Clothes
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.log.ui.model.FeelingWithLabel
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.WeatherParams
import com.youllbecold.trustme.log.ui.model.feelingName
import com.youllbecold.trustme.log.ui.model.icon
import com.youllbecold.trustme.log.ui.model.worstFeeling
import com.youllbecold.trustme.recommend.ui.mappers.getTemperatureString
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import java.time.LocalTime


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExpandedLogCardContent(
    log: LogState,
    editAction: () -> Unit,
    deleteAction: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(INSIDE_PADDING.dp),
        modifier = Modifier.padding(SPACE_BETWEEN_ITEMS.dp)
    ) {
        LogCardHeaderSection(log = log)

        LogCardWeatherSection(log.weather)

        if (log.clothes.isNotEmpty()) {
            ThemedHorizontalDivider()

            LogCardClothesSection(log.clothes)
        }

        ThemedHorizontalDivider()

        LogCardFeelingsSection(log.feelings)

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

@Composable
private fun LogCardHeaderSection(
    log: LogState,
    modifier: Modifier = Modifier
) {
    val worstFeeling = log.feelings.worstFeeling

    Column(modifier = modifier) {
        IconText(
            text = stringResource(worstFeeling.feelingName),
            iconType = worstFeeling.icon
        )

        val dateTime = "${log.date.formatDate()} ${log.timeFrom.formatTime()} - ${log.timeTo.formatTime()}"

        Spacer(modifier = Modifier.height(ITEMS_PADDING.dp))
        Text(
            text = dateTime,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = DATE_ALPHA)
        )
    }
}

@Composable
private fun LogCardWeatherSection(
    weather: WeatherParams?,
    modifier: Modifier = Modifier
) {
    weather?.let {
        val temperatureWithUnits = LocalContext.current.getTemperatureString(it.avgTemperature, it.useCelsiusUnits)
        val weatherInfo = stringResource(R.string.log_detail_weather_info, temperatureWithUnits)

        Text(
            text = weatherInfo,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = DATE_ALPHA),
            modifier = modifier
        )
    }
}

@Composable
private fun LogCardClothesSection(
    clothes: PersistentSet<Clothes>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        clothes.forEach {
            IconText(
                text = stringResource(it.name),
                iconType = it.icon,
                modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
            )
        }
    }
}

@Composable
private fun LogCardFeelingsSection(
    feelings: PersistentList<FeelingWithLabel>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        feelings.forEach { feeling ->
            val label = stringResource(feeling.label)
            val feelingText = stringResource(feeling.feeling.feelingName)
            IconText(
                text = "$label $feelingText",
                iconType = feeling.feeling.icon,
                modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
            )
        }
    }
}

private const val SPACE_BETWEEN_ACTIONS = 8
private const val SPACE_BETWEEN_ITEMS = 16
private const val DATE_ALPHA = 0.6f
private const val ITEMS_PADDING = 8
private const val INSIDE_PADDING = 16

@Preview(showBackground = true)
@Composable
private fun ExpandedLogCardContentPreview() {
    val time = LocalTime.now()

    YoullBeColdTheme {
        ExpandedLogCardContent(
            log = LogState(
                dateTimeState = DateTimeState(
                    date = DateState(2022, 1, 1),
                    timeFrom = TimeState(time.hour, time.minute),
                    timeTo = TimeState(time.hour + 1, time.minute)
                ),
                feelings = persistentListOf(),
                clothes = persistentSetOf(
                    ClothesModel.SHORT_SLEEVE.toClothes(),
                    ClothesModel.SHORTS.toClothes()
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