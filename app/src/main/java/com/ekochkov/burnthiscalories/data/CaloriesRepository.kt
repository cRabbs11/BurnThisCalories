package com.ekochkov.burnthiscalories.data

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ekochkov.burnthiscalories.data.dao.ProfileDao
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.data.entity.Profile
import com.ekochkov.burnthiscalories.data.entity.TestEntity
import com.ekochkov.burnthiscalories.util.Constants
import com.ekochkov.burnthiscalories.services.BurnCaloriesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class CaloriesRepository(private val context: Context, private val profileDao: ProfileDao) {

    private var burnList = arrayListOf<Product>()

    suspend fun getProfile(): Profile? {
        return profileDao.getProfile()
    }

    fun getProfileFlow() = profileDao.getProfileFlow()

    suspend fun saveProfile(profile: Profile) {
        profileDao.insertProfile(profile)
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

    fun startBurnService() {

        //Создаем интент
        MainScope().launch(Dispatchers.IO) {
            val intent = Intent(context, BurnCaloriesService::class.java)
            val bundle = Bundle()
            bundle.putSerializable(Constants.PROFILE_KEY, getProfile())
            bundle.putSerializable(Constants.BURN_LIST_KEY, burnList)
            intent.putExtra(Constants.BUNDLE_KEY, bundle)
            //Запускаем сервис, передав в метод интент
            context.startService(intent)
        }
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

    fun getBurnEventByStatusFlow(eventStatus: Int) = profileDao.getBurnEventByStatusFlow(eventStatus)
}