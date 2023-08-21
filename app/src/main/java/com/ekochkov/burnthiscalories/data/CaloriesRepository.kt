package com.ekochkov.burnthiscalories.data

import android.content.Context
import com.ekochkov.burnthiscalories.data.dao.ProfileDao
import com.ekochkov.burnthiscalories.data.entity.*
import com.ekochkov.burnthiscalories.util.Constants
import kotlinx.coroutines.flow.map


class CaloriesRepository(private val context: Context, private val profileDao: ProfileDao) {

    private var burnList = arrayListOf<Product>()

    suspend fun getProfile(): Profile? {
        return profileDao.getProfile()?.toProfile()
    }

    fun getProfileFlow() = profileDao.getProfileFlow().map { it?.toProfile() }

    suspend fun saveProfile(profile: Profile): Long {
        return profileDao.insertProfile(profile.toProfileDB())
    }

    suspend fun ifProfileExist(): Boolean {
        return profileDao.getProfile()!=null
    }

    suspend fun getProducts() : List<Product> {
        return profileDao.getProducts()
    }

    fun getProductsFlow() = profileDao.getProductsFlow()

    fun getProductFlow(id: Int) = profileDao.getProductFlow(id)

    suspend fun getProduct(name: String) = profileDao.getProductByName(name)

    suspend fun getProduct(id: Int) = profileDao.getProductById(id)

    suspend fun saveProducts(product: Product) {
        profileDao.saveProduct(product)
    }

    suspend fun saveProduct(product: Product) = profileDao.saveProduct(product)

    suspend fun deleteProduct(product: Product) = profileDao.deleteProduct(product)

    suspend fun updateProduct(product: Product) = profileDao.updateProduct(product)

    suspend fun saveProducts(list: List<Product>) = profileDao.saveProducts(list)

    fun addProductInBurnList(product: Product) {
        burnList.add(product)
    }

    fun removeProductFromBurnList(product: Product) {

    }

    fun clearBurnList() {
        burnList.clear()
    }

    fun getBurnList() : List<Product> {
        return burnList
    }

    fun getCaloriesInBurnList(): Int {
        var calories = 0
        burnList.forEach {
            calories+=it.calory
        }
        return calories
    }

    suspend fun saveBurnEvent(burnEvent: BurnEvent) {
        profileDao.saveBurnEvent(burnEvent)
    }

    suspend fun updateBurnEvent(burnEvent: BurnEvent): Int {
        return profileDao.updateBurnEvent(burnEvent)
    }

    suspend fun getBurnEvent(id: Int): BurnEvent? = profileDao.getBurnEventById(id)

    fun getBurnEventsFlow() = profileDao.getBurnEventsFlow()

    suspend fun getBurnEventInProgress(): BurnEvent? = profileDao.getBurnEventBystatus(Constants.BURN_EVENT_STATUS_IN_PROGRESS)

    suspend fun insertTestEntity(testEntity: TestEntity) = profileDao.insertTest(testEntity)

    fun getBurnEventFlow(id: Int) = profileDao.getBurnEventFlow(id)

    fun getBurnEventsByStatusFlow(eventStatus: Int) = profileDao.getBurnEventsByStatusFlow(eventStatus)
}