package com.youllbecold.logdatabase.model

enum class Clothes(val category: Category) {
    HEAD_SCARF(Category.HATS),
    BASEBALL_HAT(Category.HATS),
    BEANIE(Category.HATS),
    WINTER_HAT(Category.HATS),

    TANK_TOP(Category.TOPS),
    CROP_TOP(Category.TOPS),
    SHORT_SLEEVE(Category.TOPS),
    LONG_SLEEVE(Category.TOPS),
    SHIRT(Category.TOPS),

    SWEATER(Category.HOODIES),
    CARDIGAN(Category.HOODIES),
    JUMPER(Category.HOODIES),
    HOODIE(Category.HOODIES),

    LIGHT_JACKET(Category.JACKETS),
    JEAN_JACKET(Category.JACKETS),
    LEATHER_JACKET(Category.JACKETS),
    WINTER_COAT(Category.JACKETS),
    WINTER_JACKET(Category.JACKETS),

    SHORT_SKIRT(Category.BOTTOMS),
    LONG_SKIRT(Category.BOTTOMS),
    SHORTS(Category.BOTTOMS),
    LEGGINGS(Category.BOTTOMS),
    JEANS(Category.BOTTOMS),
    WARM_PANTS(Category.BOTTOMS),

    FLIP_FLOPS(Category.SHOES),
    SANDALS(Category.SHOES),
    TENNIS_SHOES(Category.SHOES),
    WINTER_SHOES(Category.SHOES),

    SHORT_TSHIRT_DRESS(Category.FULL_BODY),
    LONG_TSHIRT_DRESS(Category.FULL_BODY),
    SLEVESLESS_LONG_DRESS(Category.FULL_BODY),
    SLEEVELESS_SHORT_DRESS(Category.FULL_BODY),
    LONG_SLEEVE_LONG_DRESS(Category.FULL_BODY),
    LONG_SLEEVE_SHORT_DRESS(Category.FULL_BODY),

    FINGERLESS_GLOVES(Category.ACCESSORIES),
    GLOVES(Category.ACCESSORIES),
    WINTER_GLOVES(Category.ACCESSORIES),

    SCARF(Category.ACCESSORIES),
    WINTER_SCARF(Category.ACCESSORIES),

    TIGHTS(Category.ACCESSORIES),
    SUNGLASSES(Category.ACCESSORIES);

    enum class Category {
        HATS, TOPS, HOODIES, JACKETS, BOTTOMS, SHOES, FULL_BODY, ACCESSORIES
    }
}
