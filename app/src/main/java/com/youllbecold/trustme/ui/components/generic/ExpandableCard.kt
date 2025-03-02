package com.youllbecold.trustme.ui.components.generic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.icontext.IconText
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun ExpandableCard(
    nonExpandedContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ThemedCard(
        modifier = modifier.clickable(onClick = { expanded = !expanded }),
    ) {
        if (expanded) {
            expandedContent()
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                nonExpandedContent()

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.expand_card_action),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .clickable(onClick = { expanded = !expanded })
                        .padding(INSIDE_PADDING.dp)
                )
            }
        }
    }
}

private const val INSIDE_PADDING = 16

@Preview
@Composable
private fun ExpandableCardPreview() {
    YoullBeColdTheme {
        ExpandableCard(
            nonExpandedContent = {
                IconText(
                    text = "Title",
                    iconType = IconType.Sun
                )
            },
            expandedContent = {
                IconText(
                    text = "Expanded content",
                    iconType = IconType.Sun
                )
            }
        )
    }
}