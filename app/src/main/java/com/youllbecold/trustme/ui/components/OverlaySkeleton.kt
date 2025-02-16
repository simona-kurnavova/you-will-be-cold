package com.youllbecold.trustme.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.utils.center
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = PADDING_HORIZONTAL.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        image?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(SPACE_UNDER_IMAGE.dp))
        }

        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineLarge.center(),
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(SPACE_BETWEEN_ITEMS.dp))

        Text(
            text = stringResource(subtitle),
            style = MaterialTheme.typography.bodyMedium.center(),
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(SPACE_BETWEEN_ITEMS.dp))

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
private const val PADDING_HORIZONTAL = 18
private const val SPACE_UNDER_IMAGE = 24
private const val SPACE_BETWEEN_ITEMS = 12

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun OverlaySkeletonPreview() {
    YoullBeColdTheme {
        OverlaySkeleton(
            title = R.string.welcome_screen_title,
            subtitle = R.string.welcome_screen_description,
            buttonText = R.string.welcome_screen_button_text,
            action = {},
            image = R.drawable.thermometer_0,
        )
    }
}