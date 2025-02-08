package com.youllbecold.trustme.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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
        TimePickerDialog(
            onDismiss = { onDismiss() },
            onConfirm = { onChange(LocalTime.of(timePickerState.hour, timePickerState.minute)) }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
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