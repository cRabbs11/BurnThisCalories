package com.ekochkov.burnthiscalories.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ekochkov.burnthiscalories.data.AppDataBase
import java.io.Serializable

@Entity(tableName = AppDataBase.PRODUCT_TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category") val category: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "calory") val calory: Int,
    @ColumnInfo(name = "isCustom") val isCustom: Boolean,
): Serializable {

    companion object {
        val CATEGORY_FOOD = 1
        val CATEGORY_DRINK = 2
        val CATEGORY_OTHER = 3
    }
}

