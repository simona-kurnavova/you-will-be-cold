package com.youllbecold.trustme.ui.components.generic

import android.content.res.Configuration
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.attributes.IconAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultSmallIconAttr
import com.youllbecold.trustme.ui.components.utils.rememberVector
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme

@Composable
fun ThemedIcon(
    iconType: IconType,
    modifier: Modifier = Modifier,
    iconAttr: IconAttr = defaultSmallIconAttr(),
) {
    Icon(
        painter = rememberVector(iconType.resource),
        contentDescription = iconAttr.contentDescription,
        tint = colorResource(iconType.color),
        modifier = modifier.size(iconAttr.size)
    )
}

enum class IconType(
    @DrawableRes val resource: Int,
    @ColorRes val color: Int,
    @ColorRes val bgColor: Int = R.color.icon_bg,
) {
    // Weather
    Sun(R.drawable.ic_sun, R.color.icon_orange),
    Cloud(R.drawable.ic_cloud, R.color.icon_gray),
    Rain(R.drawable.ic_rain, R.color.icon_blue),
    Snowflake(R.drawable.ic_snowflake, R.color.icon_snow),
    Lightning(R.drawable.ic_lightning, R.color.icon_orange),
    Fog(R.drawable.ic_fog, R.color.icon_gray),
    Droplet(R.drawable.ic_droplet, R.color.icon_blue),
    Fire(R.drawable.ic_fire, R.color.icon_red),
    Wind(R.drawable.ic_wind, R.color.icon_gray),
    Popsicle(R.drawable.ic_popsicle, R.color.icon_blue),
    Thermometer(R.drawable.ic_thermometer, R.color.icon_default),

    // Clothes
    TankTop(R.drawable.ic_tank_top, R.color.icon_pink),
    TShirt(R.drawable.ic_tshirt, R.color.icon_gray),
    Shirt(R.drawable.ic_shirt, R.color.icon_gray),
    Sweater(R.drawable.ic_sweater, R.color.icon_orange),
    Jacket(R.drawable.ic_jacket, R.color.icon_blue),
    Pants(R.drawable.ic_pants, R.color.icon_blue),
    Shorts(R.drawable.ic_shorts, R.color.icon_blue),
    Dress(R.drawable.ic_dress, R.color.icon_pink),
    Skirt(R.drawable.ic_skirt, R.color.icon_pink),
    Hat(R.drawable.ic_hat, R.color.icon_gray),
    Shoes(R.drawable.ic_stocking, R.color.icon_gray),
    Sunglasses(R.drawable.ic_sunglasses, R.color.icon_gray),

    // Emoji
    SmileEmoji(R.drawable.ic_smile, R.color.icon_default),
    SadEmoji(R.drawable.ic_sad, R.color.icon_default),

    // Utility
    Trash(R.drawable.ic_trash, R.color.icon_default),
    Location(R.drawable.ic_location, R.color.icon_default),
    Calendar(R.drawable.ic_calendar, R.color.icon_default),
    Timer(R.drawable.ic_time, R.color.icon_default),
    Pencil(R.drawable.ic_pencil, R.color.icon_default),
    Book(R.drawable.ic_book, R.color.icon_default),
    Tool(R.drawable.ic_tool, R.color.icon_default),
    Plus(R.drawable.ic_plus, R.color.icon_default),
    Person(R.drawable.ic_person, R.color.icon_default),
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ThemedIconPreview() {
    YoullBeColdTheme {

    }
}