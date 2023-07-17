package com.ekochkov.burnthiscalories.data

import com.ekochkov.burnthiscalories.data.entity.Product

class PrePopulateDB {
    fun getPrepolulateProducts(): List<Product> {
        val list = arrayListOf<Product>()
        val fanta = Product(
            name = "Fanta 0,33мл",
            category = Product.CATEGORY_FOOD,
            description = "газировка в банке",
            calory = 500,
            isCustom = false
        )

        val laysNormal = Product(
            name = "чипсы Lays",
            category = Product.CATEGORY_FOOD,
            description = "чипсы 70 г",
            calory = 500,
            isCustom = false
        )

        val laysBig = Product(
            name = "чипсы Lays",
            category = Product.CATEGORY_FOOD,
            description = "чипсы 150 г",
            calory = 100,
            isCustom = false
        )

        val tvorog = Product(
            name = "Творог простоквашино 5%",
            category = Product.CATEGORY_FOOD,
            description = "65 г",
            calory = 123,
            isCustom = false
        )

        val rise = Product(
            name = "Рис белый вареный",
            category = Product.CATEGORY_FOOD,
            description = "100 г",
            calory = 116,
            isCustom = false
        )
        val buckwheat = Product(
            name = "Гречка вареная",
            category = Product.CATEGORY_FOOD,
            description = "100 г",
            calory = 343,
            isCustom = false
        )
        val pasta = Product(
            name = "Макароны отварные",
            category = Product.CATEGORY_FOOD,
            description = "100 г",
            calory = 371,
            isCustom = false
        )
        val boiledSausage = Product(
            name = "Колбаса вареная",
            category = Product.CATEGORY_FOOD,
            description = "100 г",
            calory = 257,
            isCustom = false
        )
        val milkSausage = Product(
            name = "Сосиски молочные",
            category = Product.CATEGORY_FOOD,
            description = "100 г",
            calory = 266,
            isCustom = false
        )
        val snikers = Product(
            name = "Snikers",
            category = Product.CATEGORY_FOOD,
            description = "50 г",
            calory = 250,
            isCustom = false
        )
        val snikersSuper = Product(
            name = "Snikers Super",
            category = Product.CATEGORY_FOOD,
            description = "100 г",
            calory = 500,
            isCustom = false
        )
        val cocaCola033 = Product(
            name = "Coca cola 330мл",
            category = Product.CATEGORY_FOOD,
            description = "330мл",
            calory = 139,
            isCustom = false
        )
        val cocaCola050 = Product(
            name = "Coca cola 500мл",
            category = Product.CATEGORY_FOOD,
            description = "500мл",
            calory = 210,
            isCustom = false
        )
        val cocaCola1 = Product(
            name = "Coca cola 1л",
            category = Product.CATEGORY_FOOD,
            description = "1л",
            calory = 420,
            isCustom = false
        )
        val blackBread = Product(
            name = "Кусок черного хлеба",
            category = Product.CATEGORY_FOOD,
            description = "20г",
            calory = 233,
            isCustom = false
        )

        list.add(fanta)
        list.add(laysNormal)
        list.add(laysBig)
        list.add(tvorog)
        list.add(rise)
        list.add(buckwheat)
        list.add(pasta)
        list.add(boiledSausage)
        list.add(milkSausage)
        list.add(snikers)
        list.add(snikersSuper)
        list.add(cocaCola033)
        list.add(cocaCola050)
        list.add(cocaCola1)
        list.add(blackBread)

        return list
    }
}