package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.ui.components.TimePicker
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.components.utils.formatTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.LocalTime

@Composable
fun TimeInput(
    time: ImmutableTime,
    onTimeSelected: (ImmutableTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }

    ClickableText(
        text = time.time.formatTime(),
        onClick = { showTimePicker = true },
        iconType = IconType.Timer,
        modifier = modifier,
    )

    TimePicker(
        initial = time,
        onDismiss = { showTimePicker = false },
        onChange = {
            onTimeSelected(it)
            showTimePicker = false
        },
        showPicker = showTimePicker,
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun TimeInputPreview() {
    YoullBeColdTheme {
        TimeInput(
            time = ImmutableTime(LocalTime.now()),
            onTimeSelected = {},
        )
    }
}