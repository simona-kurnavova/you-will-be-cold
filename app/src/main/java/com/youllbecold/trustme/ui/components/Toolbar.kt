package com.youllbecold.trustme.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.youllbecold.trustme.R

/**
 * Toolbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar() {
    val context = LocalContext.current
    TopAppBar(
        title = { Text(text = context.getString(R.string.app_name)) }
    )
}
