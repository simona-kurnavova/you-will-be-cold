package com.youllbecold.trustme.ui.components

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
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    initial: LocalTime,
    onDismiss: () -> Unit,
    onChange: (LocalTime) -> Unit,
    showPicker: Boolean,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initial.hour,
        initialMinute = initial.minute,
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
                        onChange(LocalTime.of(timePickerState.hour, timePickerState.minute))
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
            initial = LocalTime.now(),
            showPicker = true,
            onChange = {},
            onDismiss = {},
        )
    }
}