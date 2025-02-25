package com.youllbecold.trustme.ui.components

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun LogExitDialog(
    onConfirmation: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(stringResource(R.string.dialog_exit_title)) },
        text = { Text(stringResource(R.string.dialog_exit_message)) },
        onDismissRequest = { onDismiss() },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.dialog_dismiss))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(stringResource(R.string.dialog_ok))
            }
        },

    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun XPreview() {
    YoullBeColdTheme {
        LogExitDialog(
            onConfirmation = {},
            onDismiss = {}
        )
    }
}