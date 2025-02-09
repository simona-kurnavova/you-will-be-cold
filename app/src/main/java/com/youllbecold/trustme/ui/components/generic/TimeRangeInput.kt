package com.youllbecold.trustme.ui.components.generic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun TimeRangeInput(
    fromTime: LocalTime,
    toTime: LocalTime,
    onFromTimeClick: () -> Unit,
    onToTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        LabeledClickableText(
            label = "from",
            icon = R.drawable.ic_clock,
            text = fromTime.formatTime(),
            onClick = onFromTimeClick,
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.padding(PADDING_BETWEEN_ITEMS.dp))

        LabeledClickableText(
            label = "to",
            icon = R.drawable.ic_clock,
            text = toTime.formatTime(),
            onClick = onToTimeClick,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

private const val PADDING_BETWEEN_ITEMS = 8

private fun LocalTime.formatTime(): String {
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    return this.format(formatter)
}

@Preview(showBackground = true)
@Composable
private fun TimeRangeInputPreview() {
    YoullBeColdTheme {
        TimeRangeInput(
            fromTime = LocalTime.now(),
            toTime = LocalTime.now(),
            onFromTimeClick = {},
            onToTimeClick = {}
        )
    }
}