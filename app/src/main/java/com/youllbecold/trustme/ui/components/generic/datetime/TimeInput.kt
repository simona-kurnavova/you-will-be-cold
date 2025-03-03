package com.youllbecold.trustme.ui.components.generic.datetime

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.icontext.ClickableText
import com.youllbecold.trustme.ui.components.utils.TimeState
import com.youllbecold.trustme.ui.components.utils.formatTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.LocalTime

@Composable
fun TimeInput(
    initialTime: TimeState,
    onTimeSelected: (TimeState) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by rememberSaveable { mutableStateOf(false) }

    val localTime = LocalTime.of(initialTime.hour, initialTime.minute)

    ClickableText(
        text = localTime.formatTime(),
        onClick = { showTimePicker = true },
        iconType = IconType.Timer,
        modifier = modifier,
    )

    TimePicker(
        initialTime = initialTime,
        onDismiss = { showTimePicker = false },
        onChange = { time: TimeState ->
            onTimeSelected(time)
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
            initialTime = TimeState(12, 0),
            onTimeSelected = {},
        )
    }
}