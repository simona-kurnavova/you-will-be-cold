package com.youllbecold.trustme.ui.components.generic.datetime

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    initial: ImmutableTime,
    onDismiss: () -> Unit,
    onChange: (ImmutableTime) -> Unit,
    showPicker: Boolean,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initial.time.hour,
        initialMinute = initial.time.minute,
    )

    if (showPicker) {
        AlertDialog(
            onDismissRequest = onDismiss,
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(R.string.dialog_dismiss))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onChange(
                            ImmutableTime(
                                LocalTime.of(timePickerState.hour, timePickerState.minute)
                            )
                        )
                    }
                ) {
                    Text(stringResource(R.string.dialog_ok))
                }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }
}

@Preview
@Composable
fun DialWithDialogExamplePreview() {
    YoullBeColdTheme {
        TimePicker(
            initial = ImmutableTime(LocalTime.now()),
            showPicker = true,
            onChange = {},
            onDismiss = {},
        )
    }
}