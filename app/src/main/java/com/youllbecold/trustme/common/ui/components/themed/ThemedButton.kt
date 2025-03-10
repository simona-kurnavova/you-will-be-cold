package com.youllbecold.trustme.common.ui.components.themed

import android.content.res.Configuration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun ThemedButton(
    text: String,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.PRIMARY,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor =  when(type) {
                ButtonType.PRIMARY -> MaterialTheme.colorScheme.primaryContainer
                ButtonType.SECONDARY -> MaterialTheme.colorScheme.secondaryContainer
            }
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = when(type) {
                ButtonType.PRIMARY -> MaterialTheme.colorScheme.onPrimaryContainer
                ButtonType.SECONDARY -> MaterialTheme.colorScheme.onSecondaryContainer
            }
        )
    }
}

enum class ButtonType {
    PRIMARY,
    SECONDARY,
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PrimaryButtonPreview() {
    YoullBeColdTheme {
        ThemedButton(
            text = stringResource(R.string.app_name),
            type = ButtonType.SECONDARY,
            onClick = { }
        )
    }
}