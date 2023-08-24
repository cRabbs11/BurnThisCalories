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
    var intent: Intent? = null
    private var burnEventJob: Job? = null
    private var isBurnEventServiceIsRunning = false

    init {
        MainScope().launch(Dispatchers.IO) {
            getIsBurnEventServiceIsRunningStateFlow().collect{
                isBurnEventServiceIsRunning = it
            }
        }
    }

    fun getIsBurnEventServiceIsRunningStateFlow(): Flow<Boolean>  {
        return BurnEventForegroundService.isServiceRunningFlow().asStateFlow()
    }

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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun startBurnEvent(): Result<Unit> {
        if (isProfileExist() && !isBurnEventServiceIsRunning) {
            burnEventJob = CoroutineScope(Job()).launch(Dispatchers.Default) {
                val burnEvent = BurnEvent(
                    productsId = productToBurnList,
                    caloriesBurned = 0
                )
                saveBurnEvent(burnEvent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(repository.getLastBurnEvent()!!.id)
                }
            }
        }
        return Result.success(Unit)
    }

    suspend fun resumeBurnEvent(burnEventId: Int) {
        println("resume burning")
        if (!isBurnEventServiceIsRunning) {
            println("foreground service is not created, run it!")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(burnEventId)
            }
        } else {
            println("foreground service already running")
        }
    }

    suspend fun saveBurnEvent(burnEvent: BurnEvent) {
        repository.saveBurnEvent(burnEvent)
    }

    suspend fun getBurnEventInProgress() = repository.getBurnEventInProgress()

    fun getBurnEventsFlow() = repository.getBurnEventsFlow()

    fun getBurnEventFlow(id: Int) = repository.getBurnEventFlow(id)

    suspend fun getBurnEvent(id: Int) = repository.getBurnEvent(id)

    fun stopBurnEvent() {
        stopForegroundService()
    }

    suspend fun updateBurnEvent(burnEvent: BurnEvent) {
        repository.updateBurnEvent(burnEvent)
    }

    fun getBurnEventsByStatusFlow(eventStatus: Int) = repository.getBurnEventsByStatusFlow(eventStatus)

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startForegroundService(burnEventId: Int) {
        var currentIntent = Intent(context, BurnEventForegroundService::class.java) // Build the intent for the service
        currentIntent.putExtra(Constants.BURN_EVENT_ID_KEY, burnEventId)
        intent = currentIntent
        context.startForegroundService(intent)
    }

    private fun stopForegroundService() {
        intent?.let {
            println("stop foreground service")
            context.stopService(it)
        }
    }
}