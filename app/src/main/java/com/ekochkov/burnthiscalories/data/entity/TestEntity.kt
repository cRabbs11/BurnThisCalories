package com.ekochkov.burnthiscalories.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ekochkov.burnthiscalories.data.AppDataBase

@Entity(tableName = AppDataBase.TEST_TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class TestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "time") val time: Long,
    )