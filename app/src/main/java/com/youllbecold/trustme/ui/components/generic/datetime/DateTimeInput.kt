package com.youllbecold.trustme.ui.components.generic.datetime

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DateTimeInput(
    date: ImmutableDate,
    timeFrom: ImmutableTime,
    timeTo: ImmutableTime,
    onDateChanged: (ImmutableDate) -> Unit,
    onFromTimeSelected: (ImmutableTime) -> Unit,
    onToTimeSelected: (ImmutableTime) -> Unit,
    modifier: Modifier = Modifier,
    allowFuture: Boolean = false,
) {
    Column(modifier = modifier) {
        DateInput(
            date = date,
            onDateSelected = { onDateChanged(it) },
            allowFuture = allowFuture,
        )

        Spacer(modifier = Modifier.height(PADDING_BETWEEN_DATETIME.dp))

        TimeRangeInput(
            fromTime = timeFrom,
            toTime = timeTo,
            onFromTimeSelected = onFromTimeSelected,
            onToTimeSelected = onToTimeSelected,
        )
    }
}

private const val PADDING_BETWEEN_DATETIME = 8

@Preview
@Composable
private fun DateTimeInputPreview() {
    YoullBeColdTheme {
        DateTimeInput(
            date = ImmutableDate(LocalDate.now()),
            timeFrom = ImmutableTime(LocalTime.now()),
            timeTo = ImmutableTime(LocalTime.now().plusHours(1)),
            onDateChanged = {},
            onFromTimeSelected = {},
            onToTimeSelected = {},
        )
    }
}
