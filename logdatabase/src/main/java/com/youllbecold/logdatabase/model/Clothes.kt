package com.youllbecold.logdatabase.model

enum class Clothes(val category: Category) {
    BASEBALL_HAT(Category.HATS),
    WINTER_HAT(Category.HATS),

    TANK_TOP(Category.TOPS),
    SHORT_SLEEVE(Category.TOPS),
    LONG_SLEEVE(Category.TOPS),

    LIGHT_JACKET(Category.JACKETS),
    WINTER_JACKET(Category.JACKETS),

    SHORT_SKIRT(Category.BOTTOMS),
    LONG_SKIRT(Category.BOTTOMS),
    SHORTS(Category.BOTTOMS),
    LEGGINGS(Category.BOTTOMS),
    JEANS(Category.BOTTOMS),
    WARM_PANTS(Category.BOTTOMS),

    SANDALS(Category.SHOES),
    TENNIS_SHOES(Category.SHOES),
    WINTER_SHOES(Category.SHOES),

    SHORT_DRESS(Category.FULL_BODY),
    LONG_DRESS(Category.FULL_BODY),

    TIGHTS(Category.ACCESSORIES),
    SCARF(Category.ACCESSORIES),
    GLOVES(Category.ACCESSORIES),
    SUNGLASSES(Category.ACCESSORIES);

    enum class Category {
        HATS, TOPS, JACKETS, BOTTOMS, SHOES, FULL_BODY, ACCESSORIES
    }
}
