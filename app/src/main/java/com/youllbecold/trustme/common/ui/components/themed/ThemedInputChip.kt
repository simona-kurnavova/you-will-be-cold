package com.youllbecold.trustme.common.ui.components.themed

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.InputChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.InputChipDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.attributes.IconAttr
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

@Composable
fun ThemedInputChip(
    text: String,
    iconType: IconType,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    InputChip(
        onClick = {
            onRemove()
            enabled = !enabled
        },
        label = { ThemedText(text) },
        selected = enabled,
        leadingIcon = {
            ThemedIcon(
                iconType = iconType,
                iconAttr = IconAttr(size = InputChipDefaults.AvatarSize)
            )
        },
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = stringResource(R.string.content_desc_x_remove),
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
        modifier = modifier.wrapContentSize(),
        colors = InputChipDefaults.inputChipColors().copy(
            selectedContainerColor = colorResource(iconType.bgColor),
            selectedLabelColor = colorResource(iconType.color),
            selectedTrailingIconColor = colorResource(iconType.color),
        )
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ThemedChipPreview() {
    YoullBeColdTheme {
         ThemedInputChip(
            text = "Long dress",
            iconType = IconType.Dress,
            onRemove = { }
        )
    }
}
