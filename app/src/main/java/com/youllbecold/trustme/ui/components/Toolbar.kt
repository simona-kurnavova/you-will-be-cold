package com.youllbecold.trustme.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.trustme.R

/**
 * Toolbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    title: String = stringResource(R.string.app_name)
) {
    TopAppBar(
        title = { Text(text = title) }
    )
}
