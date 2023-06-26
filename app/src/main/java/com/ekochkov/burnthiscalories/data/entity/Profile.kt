package com.ekochkov.burnthiscalories.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ekochkov.burnthiscalories.data.AppDataBase
import java.io.Serializable

@Entity(tableName = AppDataBase.PROFILE_TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class Profile (
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "height") val height: String,
    @ColumnInfo(name = "weight") val weight: String,
    @ColumnInfo(name = "age") val age: Int,
    @ColumnInfo(name = "sex") val sex: Int
): Serializable {
    companion object {
        val MALE = 0
        val FEMALE = 1
    }
}

