package com.youllbecold.trustme.ui.components.generic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.ui.components.generic.attributes.defaultMediumTextAttr

@Composable
fun SelectRows(
    items: List<SelectableItemContent>,
    onItemsSelected: (List<Int>) -> Unit,
    modifier: Modifier = Modifier,
    preSelected: Set<Int> = emptySet(),
    allowMultipleSelection: Boolean = false,
    maxSelectedItems: Int = 1,
    bgDefinition: BackgroundDefinition = BackgroundDefinition(
        bgColor = MaterialTheme.colorScheme.background,
        selectedBgColor = MaterialTheme.colorScheme.primary,
        bgShape = RoundedCornerShape(ITEM_CORNER_RADIUS.dp),
        borderWidth = 0,
    ),
) {
   var selectedItems by rememberSaveable { mutableStateOf(preSelected) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        items.forEachIndexed { index, selectableItem ->

            SelectableItem(
                title = selectableItem.title,
                isSelected = selectedItems.contains(index),
                onSelect = { selected ->
                    if (!allowMultipleSelection) {
                        selectedItems = when {
                            selected -> setOf(index)
                            else -> setOf()
                        }
                    } else {
                        selectedItems = when {
                            selected && selectedItems.size < maxSelectedItems -> selectedItems + setOf(index)
                            selected -> selectedItems
                            else -> selectedItems - setOf(index)
                        }
                    }
                    onItemsSelected(selectedItems.toList())
                },
                modifier = Modifier.fillMaxWidth(),
                bgDefinition = bgDefinition,
                iconType = selectableItem.iconType,
            )

            if (index < items.size - 1) {
                Spacer(modifier = Modifier.size(PADDING_AROUND_SELECT_ROWS.dp))
            }
        }
   }
}

private const val PADDING_AROUND_SELECT_ROWS = 8

@Composable
private fun SelectableItem(
    title: String,
    isSelected: Boolean,
    onSelect: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    bgDefinition: BackgroundDefinition,
    iconType: IconType? = null,
) {
    Box(
        modifier = modifier
            .background(
                color = if (isSelected) bgDefinition.selectedBgColor.copy(SELECT_BG_ALPHA) else bgDefinition.bgColor,
                shape = bgDefinition.bgShape,
            )
            .then(
                if (bgDefinition.borderWidth > 0) {
                    Modifier.border(
                        width = bgDefinition.borderWidth.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = bgDefinition.bgShape,
                    )
                } else Modifier
            )
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = {
                    onSelect(!isSelected)
                },
            )
            .padding(PADDING_BG_INSIDE_ITEM.dp)
    ) {
        IconText(
            iconType = iconType,
            text = title,
            textAttr = defaultMediumTextAttr(),
            paddingBeforeText = PADDING_ITEM_ICON_END,
        )
    }
}

private const val PADDING_BG_INSIDE_ITEM = 18
private const val PADDING_ITEM_ICON_END = 12
private const val ITEM_CORNER_RADIUS = 16
private const val SELECT_BG_ALPHA = 0.25f

data class SelectableItemContent(
    val iconType: IconType? = null,
    val title: String,
)

data class BackgroundDefinition(
    val bgColor: Color,
    val selectedBgColor: Color,
    val bgShape: Shape,
    val borderWidth: Int
)

@Preview(showBackground = true)
@Composable
fun SelectRowsPreview() {
    SelectRows(
        items = listOf(
            SelectableItemContent(title = "Item 1", iconType = IconType.Sun),
            SelectableItemContent(title = "Item 2", iconType = IconType.Snowflake),
            SelectableItemContent(title = "Item 3"),
        ),
        preSelected = setOf(0),
        onItemsSelected = { },
    )
}
