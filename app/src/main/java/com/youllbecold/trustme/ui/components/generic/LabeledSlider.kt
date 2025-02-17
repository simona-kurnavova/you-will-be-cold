package com.youllbecold.trustme.ui.components.generic

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@SuppressLint("UnrememberedMutableInteractionSource")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledSlider(
    label: String,
    options: List<String>,
    selected: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderValue by remember { mutableFloatStateOf((selected).toFloat()) }

    val color = calculateColor(sliderValue, options.size)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.spacedBy(SPACE_BETWEEN_TEXT.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = options.getOrNull(sliderValue.toInt()) ?: "Unknown",
                style = MaterialTheme.typography.bodyLarge,
                color = color
            )
        }

        val colors = SliderDefaults.colors(
            thumbColor = color,
            activeTrackColor = color,
            inactiveTrackColor = color.copy(alpha = 0.5f),
        )

        Slider(
            value = sliderValue,
            onValueChange = {  sliderValue = it },
            onValueChangeFinished = {
                onSelected(sliderValue.toInt())
            },
            valueRange = 0f..options.size.toFloat() - 1,
            steps = options.size - 2,
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(SLIDER_HEIGHT.dp),
                    colors = colors
                )
            },
            thumb = @Composable {
                SliderDefaults.Thumb(
                    interactionSource = remember { MutableInteractionSource() },
                    thumbSize = DpSize(8.dp, 18.dp),
                    colors = colors
                )
            },
        )
    }
}

private const val SLIDER_HEIGHT = 7
private const val SPACE_BETWEEN_TEXT = 12

private fun calculateColor(sliderValue: Float, options: Int): Color {
    val fraction = 1.0f - (sliderValue / (options - 1)) // Reverse the fraction

    return Color(
        red = 1.0f - (0.4f * fraction), // Now starts at 0.6 (blue) and increases to 1.0 (red)
        green = 0.7f,  // Keeping green constant for nice shades
        blue = 0.6f + (0.4f * fraction) // Now starts at 1.0 (red) and decreases to 0.6 (blue)
    )
}


@Preview
@Composable
private fun LabeledSliderPreview() {
    YoullBeColdTheme {
        LabeledSlider(
            label = "Label",
            options = listOf("Option 1", "Option 2", "Option 3"),
            selected = 2,
            onSelected = {},
        )
    }
}