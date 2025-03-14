package com.youllbecold.trustme.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.attributes.defaultLargeTextAttr
import com.youllbecold.trustme.common.ui.attributes.ellipsized
import com.youllbecold.trustme.common.ui.components.icontext.IconText
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

/**
 * Toolbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    title: String = stringResource(R.string.app_name),
    iconType: IconType? = null,
    showInfoAction: Boolean = false,
    navigateToInfo: () -> Unit,
    backButtonAction: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            IconText(
                iconType = iconType,
                text = title,
                textAttr = defaultLargeTextAttr().ellipsized()
            )
        },
        navigationIcon = {
            backButtonAction?.let {
                IconButton(onClick = it) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (showInfoAction) {
                IconButton(onClick = navigateToInfo) {
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
            navigateToInfo = {},
            backButtonAction = {}
        )
    }
}
