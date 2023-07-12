package com.ekochkov.burnthiscalories.domain

import android.app.Service
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.ekochkov.burnthiscalories.data.CaloriesRepository
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.data.entity.Profile
import com.ekochkov.burnthiscalories.util.CaloriesCalculator
import com.ekochkov.burnthiscalories.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class Interactor(private val repository: CaloriesRepository, private val caloriesCalculator: CaloriesCalculator, private val context: Context) {

    private var productToBurnList = arrayListOf<Product>()

    fun addToProductToBurnList(product: Product) {
        productToBurnList.add(product)
    }

    fun deleteFromProductToBurnList(product: Product) {
        productToBurnList.remove(product)
    }

    fun getProductsToBurnFlow(): Flow<List<Product>> {
        return flow {
            emit(productToBurnList)
        }
    }

    suspend fun getProfile(): Profile? {
        return repository.getProfile()
    }

    fun getProfileFlow() = repository.getProfileFlow()

    suspend fun saveProfile(profile: Profile) {
        repository.saveProfile(profile)
    }

    suspend fun getProducts(): List<Product> {
        return repository.getProducts()
    }

    fun getProductsFlow() = repository.getProductsFlow()

    suspend fun getProduct(name: String) = repository.getProduct(name)

    suspend fun getProduct(id: Int) = repository.getProduct(id)

    fun getProductFlow(id: Int) = repository.getProductFlow(id)

    suspend fun saveProduct(product: Product) = repository.saveProduct(product)

    suspend fun deleteProduct(product: Product) = repository.deleteProduct(product)

    suspend fun updateProduct(product: Product) = repository.updateProduct(product)

    suspend fun saveProducts(list: List<Product>) {
        repository.saveProducts(list)
    }

    fun addProductInBurnList(product: Product) {
        repository.addProductInBurnList(product)
    }

    fun getBurnList(): List<Product> {
        return productToBurnList
    }

    suspend fun startBurnEvent(burnEvent: BurnEvent) {
        println("start burning")
        caloriesCalculator.setProfile(getProfile()!!)
        saveBurnEvent(burnEvent)
        val startedBurnEvent = getBurnEventInProgress()!!
        startStepCountSensor(startedBurnEvent)
    }

    suspend fun resumeBurnEvent(burnEvent: BurnEvent) {
        println("resume burning")
        caloriesCalculator.setProfile(getProfile()!!)
        startStepCountSensor(burnEvent)
    }


    private fun startStepCountSensor(burnEvent: BurnEvent) {
        caloriesCalculator.startCalculator(burnEvent)
    }

    fun stopBurnEvent() {
        caloriesCalculator.stopCalculator()
    }

    suspend fun saveBurnEvent(burnEvent: BurnEvent) {
        repository.saveBurnEvent(burnEvent)
    }

    suspend fun getBurnEventInProgress() = repository.getBurnEventInProgress()

    fun getBurnEventsFlow() = repository.getBurnEventsFlow()

    fun getBurnEventFlow(id: Int) = repository.getBurnEventFlow(id)

    suspend fun getBurnEvent(id: Int) = repository.getBurnEvent(id)

    fun finishEvent() {
        MainScope().launch(Dispatchers.IO) {
            var burnEvent = getBurnEventInProgress()
            val updatedBurnEvent = BurnEvent(
                id = burnEvent!!.id,
                productsId = burnEvent.productsId,
                caloriesBurned = 7227,
                eventStatus = Constants.BURN_EVENT_STATUS_DONE
            )
            repository.updateBurnEvent(updatedBurnEvent)
        }
    }

    fun getBurnEventByStatusFlow(eventStatus: Int) = repository.getBurnEventByStatusFlow(eventStatus)
}