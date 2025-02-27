package com.youllbecold.trustme.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.attributes.defaultLargeTextAttr
import com.youllbecold.trustme.ui.components.generic.icontext.IconText
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

/**
 * Toolbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    title: String = stringResource(R.string.app_name),
    iconType: IconType? = null,
showInfoAction: Boolean = false,
    navigateToInfo: () -> Unit
) {
    TopAppBar(
        title = {
            IconText(
                iconType = iconType,
                text = title,
                textAttr = defaultLargeTextAttr()
            )
        },
        actions = {
            if (showInfoAction) {
                IconButton(onClick = { navigateToInfo() }) {
                    Icon(Icons.Outlined.Info, contentDescription = stringResource(R.string.help))
                }
            }
        }
    )
}

@Preview
@Composable
private fun ToolbarPreview() {
    YoullBeColdTheme {
        Toolbar(
            title = "Title",
            iconType = IconType.Sun,
            showInfoAction = true,
            navigateToInfo = {}
        )
    }
}
