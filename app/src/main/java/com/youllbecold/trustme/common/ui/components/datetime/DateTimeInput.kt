package com.youllbecold.trustme.common.ui.components.datetime

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.common.ui.components.utils.DateState
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.components.utils.TimeState
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun DateTimeInput(
    dateTimeState: DateTimeState,
    onDatetimeChanged: (DateTimeState) -> Unit,
    modifier: Modifier = Modifier,
    allowFuture: Boolean = false,
) {
    Column(modifier = modifier) {
        DateInput(
            date = dateTimeState.date,
            onDateSelected = {
                onDatetimeChanged(dateTimeState.copy(date = it))
            },
            allowFuture = allowFuture,
        )

        Spacer(modifier = Modifier.height(PADDING_BETWEEN_DATETIME.dp))

        TimeRangeInput(
            fromTime = dateTimeState.timeFrom,
            toTime = dateTimeState.timeTo,
            onFromTimeSelected = { fromTime ->
                onDatetimeChanged(dateTimeState.copy(timeFrom = fromTime))
            },
            onToTimeSelected = { toTime ->
                onDatetimeChanged(dateTimeState.copy(timeTo = toTime))
            }
        )
    }
}

private const val PADDING_BETWEEN_DATETIME = 8

@Preview
@Composable
private fun DateTimeInputPreview() {
    YoullBeColdTheme {
        DateTimeInput(
            dateTimeState = DateTimeState(
                date = DateState(2022, 1, 1),
                timeFrom = TimeState(12, 0),
                timeTo = TimeState(13, 0)
            ),
            onDatetimeChanged = {}
        )
    }
}
