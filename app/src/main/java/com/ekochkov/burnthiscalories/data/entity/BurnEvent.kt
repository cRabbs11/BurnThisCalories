package com.ekochkov.burnthiscalories.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ekochkov.burnthiscalories.data.AppDataBase
import com.ekochkov.burnthiscalories.util.Constants

@Entity(tableName = AppDataBase.BURN_EVENT_TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class BurnEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productsId: List<Product>,
    @ColumnInfo(name = "caloriesBurned") val caloriesBurned: Int,
    @ColumnInfo(name = "eventStatus") val eventStatus: Int = Constants.BURN_EVENT_STATUS_IN_PROGRESS,
) {
}