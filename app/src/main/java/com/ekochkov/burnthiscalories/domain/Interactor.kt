package com.ekochkov.burnthiscalories.domain

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.ekochkov.burnthiscalories.data.CaloriesRepository
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.data.entity.Profile
import com.ekochkov.burnthiscalories.services.BurnEventForegroundService
import com.ekochkov.burnthiscalories.util.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception

class Interactor(private val repository: CaloriesRepository, private val context: Context) {

    private var productToBurnList = mutableListOf<Product>()
    private var burnListFlow = MutableSharedFlow<List<Product>>()
    private var finishEventJob: Job? = null
    lateinit var intent: Intent
    private var burnEventJob: Job? = null

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

    suspend fun isProfileExist(): Boolean {
        return repository.ifProfileExist()
    }

    suspend fun getProfile(): Profile? {
        return repository.getProfile()
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

    suspend fun isBurnEventRunning(): Boolean {
        return (repository.getBurnEventInProgress()!=null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun startBurnEvent(): Result<Unit> {
        if (isProfileExist() && !isBurnEventRunning()) {
            burnEventJob = CoroutineScope(Job()).launch(Dispatchers.Default) {
                val burnEvent = BurnEvent(
                    productsId = productToBurnList,
                    caloriesBurned = 0
                )
                saveBurnEvent(burnEvent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService()
                }
            }
        }
        return Result.success(Unit)
    }

    suspend fun resumeBurnEvent(burnEvent: BurnEvent) {
        println("resume burning")
        if (repository.ifProfileExist()) {
            caloriesCalculator.setProfile(repository.getProfile()!!)
            startStepCountSensor(burnEvent)
        }
    }

    suspend fun saveBurnEvent(burnEvent: BurnEvent) {
        repository.saveBurnEvent(burnEvent)
    }

    suspend fun getBurnEventInProgress() = repository.getBurnEventInProgress()

    fun getBurnEventsFlow() = repository.getBurnEventsFlow()

    fun getBurnEventFlow(id: Int) = repository.getBurnEventFlow(id)

    suspend fun getBurnEvent(id: Int) = repository.getBurnEvent(id)

    fun finishEvent() {
       finishEventJob = CoroutineScope(Job()).launch (Dispatchers.IO){
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

    suspend fun updateBurnEvent(burnEvent: BurnEvent) {
        repository.updateBurnEvent(burnEvent)
    }

    fun getBurnEventsByStatusFlow(eventStatus: Int) = repository.getBurnEventsByStatusFlow(eventStatus)

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startForegroundService() {
        intent = Intent(context, BurnEventForegroundService::class.java) // Build the intent for the service
        context.startForegroundService(intent)
    }

    fun stopForegroundService() {
        intent?.let {
            println("stop foreground service")
            context.stopService(it)
        }
    }
}