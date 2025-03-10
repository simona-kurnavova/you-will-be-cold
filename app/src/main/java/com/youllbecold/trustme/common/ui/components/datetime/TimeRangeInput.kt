package com.youllbecold.trustme.common.ui.components.datetime

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.utils.TimeState
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun TimeRangeInput(
    fromTime: TimeState,
    toTime: TimeState,
    onFromTimeSelected: (TimeState) -> Unit,
    onToTimeSelected: (TimeState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LabeledTimeInput(
            label = stringResource(R.string.time_range_from),
            time = fromTime,
            onTimeSelected = onFromTimeSelected,
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(PADDING_BETWEEN_ITEMS.dp))

        LabeledTimeInput(
            label = stringResource(R.string.time_range_to),
            time = toTime,
            onTimeSelected = onToTimeSelected,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

private const val PADDING_BETWEEN_ITEMS = 8

@Composable
private fun LabeledTimeInput(
    label: String,
    time: TimeState,
    onTimeSelected: (TimeState) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.width(PADDING_UNDER_LABEL.dp))

        TimeInput(
            initialTime = time,
            onTimeSelected = onTimeSelected,
        )
    }
}

private const val PADDING_UNDER_LABEL = 8

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun TimeRangeInputPreview() {
    YoullBeColdTheme {
        TimeRangeInput(
            fromTime = TimeState(12, 0),
            toTime = TimeState(13, 0),
            onFromTimeSelected = {},
            onToTimeSelected = {}
        )
    }
}
