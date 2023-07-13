package com.ekochkov.burnthiscalories.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ekochkov.burnthiscalories.data.dao.ProfileDao
import com.ekochkov.burnthiscalories.data.entity.*
import com.ekochkov.burnthiscalories.util.ProductsTypeConverter

@Database(entities = [
    Profile::class,
    Product::class,
    BurnEvent::class,
    TestEntity::class],
    version = 1, exportSchema = false)
@TypeConverters(ProductsTypeConverter::class)
abstract class AppDataBase: RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    companion object {
        const val DATABASE_NAME = "calories_db_name"

        const val PROFILE_TABLE_NAME = "profile_table"
        const val PRODUCT_TABLE_NAME = "product_table"
        const val TEST_TABLE_NAME = "test_table"
        const val BURN_EVENT_TABLE_NAME = "burn_event_table"

        const val SEARCH_TYPE_ARTISTS = 1
    }
}