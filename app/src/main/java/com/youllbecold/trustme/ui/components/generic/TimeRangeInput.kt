package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.utils.formatTime
import com.youllbecold.trustme.ui.components.utils.rememberVector
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.LocalTime

@Composable
fun TimeRangeInput(
    fromTime: LocalTime,
    toTime: LocalTime,
    onFromTimeClick: () -> Unit,
    onToTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LabeledClickableText(
            label = stringResource(R.string.time_range_from),
            painter = rememberVector(R.drawable.ic_clock),
            text = fromTime.formatTime(),
            onClick = onFromTimeClick,
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.width(PADDING_BETWEEN_ITEMS.dp))

        LabeledClickableText(
            label = stringResource(R.string.time_range_to),
            painter = rememberVector(R.drawable.ic_clock),
            text = toTime.formatTime(),
            onClick = onToTimeClick,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

private const val PADDING_BETWEEN_ITEMS = 8

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
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
