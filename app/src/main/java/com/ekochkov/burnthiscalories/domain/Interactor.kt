package com.ekochkov.burnthiscalories.domain

import android.content.Context
import com.ekochkov.burnthiscalories.data.CaloriesRepository
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.data.entity.Profile
import com.ekochkov.burnthiscalories.util.CaloriesCalculator
import com.ekochkov.burnthiscalories.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

class Interactor(private val repository: CaloriesRepository, private val caloriesCalculator: CaloriesCalculator, private val context: Context) {

    private var productToBurnList = mutableListOf<Product>()
    private var burnListFlow = MutableSharedFlow<List<Product>>()

    fun addProductToBurnList(product: Product) {
        productToBurnList.add(product)
        MainScope().launch {
            burnListFlow.emit(productToBurnList)
        }
    }

    fun clearProductToBurnList() {
        productToBurnList.clear()
        MainScope().launch {
            burnListFlow.emit(productToBurnList)
        }
    }

    fun getProductsToBurnStateFlow(): Flow<List<Product>> {
        return burnListFlow
    }

    fun getProductsToBurnFlow(): Flow<List<Product>> {
        return flow {
            emit(productToBurnList)
        }
    }

    fun getProfileFlow() = repository.getProfileFlow()

    suspend fun saveProfile(profile: Profile): Result<Unit> {
        try {
            repository.saveProfile(profile)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return Result.success(Unit)
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

    fun getBurnList(): List<Product> {
        return productToBurnList
    }

    suspend fun startBurnEvent(burnEvent: BurnEvent): Boolean {
        return if (repository.ifProfileExist()) {
            println("start burning")
            caloriesCalculator.setProfile(repository.getProfile()!!)
            saveBurnEvent(burnEvent)
            val startedBurnEvent = getBurnEventInProgress()!!
            startStepCountSensor(startedBurnEvent)
            clearProductToBurnList()
            true
        } else {
            false
        }
    }

    suspend fun resumeBurnEvent(burnEvent: BurnEvent) {
        println("resume burning")
        if (repository.ifProfileExist()) {
            caloriesCalculator.setProfile(repository.getProfile()!!)
            startStepCountSensor(burnEvent)
        }
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

    fun getBurnEventsByStatusFlow(eventStatus: Int) = repository.getBurnEventsByStatusFlow(eventStatus)
}