package com.youllbecold.trustme.ui.components.generic

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Skeleton for overlay screen containing image, title, description and button.
 *
 * @param title title of the overlay.
 * @param subtitle subtitle of the overlay.
 * @param buttonText text of the button.
 * @param action callback when the button is clicked.
 * @param modifier modifier for the overlay.
 * @param image image to be displayed on the overlay. Optional.
 */
@Composable
fun OverlaySkeleton(
    @StringRes title: Int,
    @StringRes subtitle: Int,
    @StringRes buttonText: Int,
    action: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes image: Int? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (image != null) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.padding(18.dp)
            )
        }

        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineLarge.center(),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 18.dp)
                .padding(horizontal = 18.dp),
        )

        Text(
            text = stringResource(subtitle),
            style = MaterialTheme.typography.bodyMedium.center(),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(18.dp),
        )

        Button(
            onClick = { action() },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Text(text = stringResource(buttonText))
        }
    }
}

private fun TextStyle.center() = this.copy(textAlign = TextAlign.Center)
