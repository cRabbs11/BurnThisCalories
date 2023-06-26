package com.ekochkov.burnthiscalories.util

import androidx.room.TypeConverter
import com.ekochkov.burnthiscalories.data.entity.Product
import com.google.gson.Gson

class ProductsTypeConverter {
    @TypeConverter
    fun fromProductsToString(sets: List<Product>): String {
        return Gson().toJson(sets)
    }

    @TypeConverter
    fun fromStringToProducts(string: String): List<Product> {
        val array = Gson().fromJson(string, Array<Product>::class.java)
        return array.toList()
    }
}