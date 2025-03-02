package com.youllbecold.trustme.ui.components.generic.datetime

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.utils.TimeState
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    initialTime: TimeState,
    onDismiss: () -> Unit,
    onChange: (TimeState) -> Unit,
    showPicker: Boolean,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
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
                            TimeState(
                                timePickerState.hour,
                                timePickerState.minute
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
private fun DialWithDialogExamplePreview() {
    YoullBeColdTheme {
        TimePicker(
            initialTime = TimeState(12, 30),
            showPicker = true,
            onChange = {},
            onDismiss = {},
        )
    }
}