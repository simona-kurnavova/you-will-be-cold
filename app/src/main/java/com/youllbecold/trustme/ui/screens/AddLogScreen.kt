package com.youllbecold.trustme.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.TimePicker
import com.youllbecold.trustme.ui.components.generic.ClickableText
import com.youllbecold.trustme.ui.components.generic.SelectRows
import com.youllbecold.trustme.ui.components.generic.SelectableItemContent
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun AdLogRoot() {
    AddLogScreen()
}

@Composable
fun AddLogScreen() {
    val currentTime = LocalTime.now()

    var rangeTimeState by remember {
        mutableStateOf(
            RangeTimeState(
                timeFrom = currentTime.minusHours(1),
                timeTo = LocalTime.now(),
                showFromPicker = false,
                showToPicker = false
            )
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PADDING_AROUND_QUESTION.dp),
        ) {
            Text(
                text = "When were you out?",
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.padding(PADDING_BETWEEN_ITEMS.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                ClickableText(
                    text = rangeTimeState.timeFrom.formatTime(),
                    onClick = {
                        rangeTimeState = rangeTimeState.copy(
                            showFromPicker = true
                        )
                    }
                )

                Spacer(modifier = Modifier.padding(PADDING_BETWEEN_ITEMS.dp))

                Text(
                    text = "-",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.padding(PADDING_BETWEEN_ITEMS.dp))

                ClickableText(
                    text = rangeTimeState.timeTo.formatTime(),
                    onClick = {
                        rangeTimeState = rangeTimeState.copy(
                            showToPicker = true
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.padding(PADDING_BETWEEN_SECTIONS.dp))

            Text(
                text = "How did you feel today?",
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.padding(PADDING_BETWEEN_ITEMS.dp))

            SelectRows(
                items = feelingItems(),
                onItemsSelected = { selectedItems ->
                    Log.d("AddLogScreen", "Selected items: $selectedItems")
                    val result = Feeling.entries.filter { selectedItems.contains(it.ordinal) }
                    Log.d("AddLogScreen", "Selected enums: $result")

                },
            )
        }

        TimePicker(
            initial = rangeTimeState.timeFrom,
            onDismiss = { rangeTimeState = rangeTimeState.copy(showFromPicker = false) },
            onChange = {
                rangeTimeState = rangeTimeState.copy(
                    timeFrom = it,
                    showFromPicker = false
                )
            },
            showPicker = rangeTimeState.showFromPicker,
        )

        TimePicker(
            initial = rangeTimeState.timeTo,
            onDismiss = { rangeTimeState = rangeTimeState.copy(showToPicker = false) },
            onChange = {
                rangeTimeState = rangeTimeState.copy(
                    timeTo = it,
                    showToPicker = false
                )
            },
            showPicker = rangeTimeState.showToPicker,
        )
    }
}

private fun LocalTime.formatTime(): String {
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    return this.format(formatter)
}

private data class RangeTimeState(
    val timeFrom: LocalTime,
    val timeTo: LocalTime,
    val showFromPicker: Boolean,
    val showToPicker: Boolean
)

@Composable
private fun feelingItems(): List<SelectableItemContent> =
    Feeling.entries.map { feeling ->
        when (feeling) {
            Feeling.VeryHot -> SelectableItemContent(
                R.drawable.ic_sun,
                stringResource(id = R.string.feeling_very_hot)
            )

            Feeling.Hot -> SelectableItemContent(
                R.drawable.ic_sun,
                stringResource(id = R.string.feeling_hot)
            )

            Feeling.Normal -> SelectableItemContent(
                R.drawable.ic_cloud,
                stringResource(id = R.string.feeling_normal)
            )

            Feeling.Cold -> SelectableItemContent(
                R.drawable.ic_snow,
                stringResource(id = R.string.feeling_cold)
            )

            Feeling.VeryCold -> SelectableItemContent(
                R.drawable.ic_snow,
                stringResource(id = R.string.feeling_very_cold)
            )
        }
    }

enum class Feeling {
    VeryHot,
    Hot,
    Normal,
    Cold,
    VeryCold,
}

private const val PADDING_BETWEEN_ITEMS = 8
private const val PADDING_BETWEEN_SECTIONS = 16
private const val PADDING_AROUND_QUESTION = 4

@Preview(showBackground = true)
@Composable
fun AdLogScreenPreview() {
    YoullBeColdTheme {
        AddLogScreen()
    }
}