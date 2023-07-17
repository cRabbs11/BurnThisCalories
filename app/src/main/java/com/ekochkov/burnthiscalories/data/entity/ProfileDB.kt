package com.ekochkov.burnthiscalories.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ekochkov.burnthiscalories.data.AppDataBase
import java.io.Serializable

@Entity(tableName = AppDataBase.PROFILE_TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class ProfileDB (
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "height") val height: String,
    @ColumnInfo(name = "weight") val weight: String,
    @ColumnInfo(name = "age") val age: Int,
    @ColumnInfo(name = "sex") val sex: Int
): Serializable

fun ProfileDB.toProfile() = Profile(
    name = name,
    height = height,
    weight = weight,
    age = age,
    sex = sex)



