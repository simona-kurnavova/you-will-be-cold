package com.youllbecold.trustme.ui.components.generic

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
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

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
                ButtonType.PRIMARY -> MaterialTheme.colorScheme.primary
                ButtonType.SECONDARY -> MaterialTheme.colorScheme.secondary
            }
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = when(type) {
                ButtonType.PRIMARY -> MaterialTheme.colorScheme.onPrimary
                ButtonType.SECONDARY -> MaterialTheme.colorScheme.onSecondary
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
fun PrimaryButtonPreview() {
    YoullBeColdTheme {
        ThemedButton(
            text = stringResource(R.string.app_name),
            type = ButtonType.SECONDARY,
            onClick = { }
        )
    }
}