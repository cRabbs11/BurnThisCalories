package com.ekochkov.burnthiscalories.data

import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        list.add(fanta)
        list.add(laysNormal)
        list.add(laysBig)
        list.add(tvorog)
        return list
    }
}