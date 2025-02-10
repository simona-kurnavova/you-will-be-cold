package com.youllbecold.logdatabase.model

enum class Clothes(val category: Category) {
    SHORT_SLEEVE(Category.TOPS),
    LONG_SLEEVE(Category.TOPS),
    SHORT_SKIRT(Category.BOTTOMS),
    SHORTS(Category.BOTTOMS),
    JEANS(Category.BOTTOMS),
    SANDALS(Category.SHOES),
    TENNIS_SHOES(Category.SHOES),
    DRESS(Category.FULL_BODY);

    enum class Category {
        HATS, TOPS, JACKETS, BOTTOMS, SHOES, FULL_BODY, ACCESSORIES
    }
}
