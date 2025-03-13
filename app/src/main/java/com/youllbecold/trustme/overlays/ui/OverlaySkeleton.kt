package com.youllbecold.trustme.overlays.ui

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.attributes.TextAttr
import com.youllbecold.trustme.common.ui.attributes.centered
import com.youllbecold.trustme.common.ui.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.common.ui.components.themed.ThemedButton
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme

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

        ThemedText(
            text = stringResource(title),
            textAttr = TextAttr(
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            ),
        )

        Spacer(modifier = Modifier.height(SPACE_BETWEEN_ITEMS.dp))

        ThemedText(
            text = stringResource(subtitle),
            textAttr = defaultMediumTextAttr().centered(),
        )

        Spacer(modifier = Modifier.height(SPACE_BETWEEN_ITEMS.dp))

        ThemedButton(
            text = stringResource(buttonText),
            onClick = action,
        )
    }
}
private const val PADDING_HORIZONTAL = 18
private const val SPACE_UNDER_IMAGE = 24
private const val SPACE_BETWEEN_ITEMS = 24

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