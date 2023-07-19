package com.ekochkov.burnthiscalories.data.dao

import androidx.room.*
import com.ekochkov.burnthiscalories.data.AppDataBase
import com.ekochkov.burnthiscalories.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT * FROM ${AppDataBase.PROFILE_TABLE_NAME} WHERE id LIKE 1")
    suspend fun getProfile() : ProfileDB?

    @Query("SELECT * FROM ${AppDataBase.PROFILE_TABLE_NAME} WHERE id LIKE 1")
    fun getProfileFlow() : Flow<ProfileDB?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileDB): Long

    @Insert
    suspend fun insertTest(testEntity: TestEntity)

    @Query("SELECT * FROM ${AppDataBase.PRODUCT_TABLE_NAME}")
    suspend fun getProducts(): List<Product>

    @Query("SELECT * FROM ${AppDataBase.PRODUCT_TABLE_NAME}")
    fun getProductsFlow(): Flow<List<Product>>

    @Query("SELECT * FROM ${AppDataBase.PRODUCT_TABLE_NAME} WHERE id LIKE:id")
    fun getProductFlow(id: Int) : Flow<Product?>

    @Query("SELECT * FROM ${AppDataBase.PRODUCT_TABLE_NAME} WHERE name LIKE:name")
    suspend fun getProductByName(name: String): Product?

    @Query("SELECT * FROM ${AppDataBase.PRODUCT_TABLE_NAME} WHERE id LIKE:id")
    suspend fun getProductById(id: Int): Product?

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProduct(product: Product): Long

    @Delete
    suspend fun deleteProduct(product: Product): Int

    @Update
    suspend fun updateProduct(product: Product): Int

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProducts(list: List<Product>): List<Long>

    @Insert
    suspend fun saveBurnEvent(burnEvent: BurnEvent)

    @Update
    suspend fun updateBurnEvent(burnEvent: BurnEvent): Int

    @Query("SELECT * FROM ${AppDataBase.BURN_EVENT_TABLE_NAME} WHERE eventStatus LIKE:eventStatus")
    suspend fun getBurnEventBystatus(eventStatus: Int): BurnEvent?

    @Query("SELECT * FROM ${AppDataBase.BURN_EVENT_TABLE_NAME} WHERE id LIKE:id")
    suspend fun getBurnEventById(id: Int): BurnEvent?

    @Query("SELECT * FROM ${AppDataBase.BURN_EVENT_TABLE_NAME} WHERE id LIKE:id")
    fun getBurnEventFlow(id: Int): Flow<BurnEvent?>

    @Query("SELECT * FROM ${AppDataBase.BURN_EVENT_TABLE_NAME}")
    suspend fun getBurnEvents(): List<BurnEvent>

    @Query("SELECT * FROM ${AppDataBase.BURN_EVENT_TABLE_NAME}")
    fun getBurnEventsFlow(): Flow<List<BurnEvent>>

    @Query("SELECT * FROM ${AppDataBase.BURN_EVENT_TABLE_NAME} WHERE eventStatus LIKE:eventStatus")
    fun getBurnEventsByStatusFlow(eventStatus: Int): Flow<List<BurnEvent>>
}