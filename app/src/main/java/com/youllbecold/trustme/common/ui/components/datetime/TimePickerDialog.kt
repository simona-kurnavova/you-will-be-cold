package com.youllbecold.trustme.common.ui.components.datetime

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.ThemedButton
import com.youllbecold.trustme.common.ui.components.utils.TimeState
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

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
                ThemedButton(
                    text = stringResource(R.string.dialog_dismiss),
                    onClick = { onDismiss() }
                )
            },
            confirmButton = {
                ThemedButton(
                    text = stringResource(R.string.dialog_ok),
                    onClick = {
                        onChange(
                            TimeState(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                        )
                    }
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ){
                    TimeInput(state = timePickerState)
                }

            }
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