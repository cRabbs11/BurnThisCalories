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

    private lateinit var sensor: Sensor
    private lateinit var sensorManager: SensorManager

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
        startSensor(caloriesCalculator, startedBurnEvent)
    }

    suspend fun resumeBurnEvent(burnEvent: BurnEvent) {
        println("resume burning")
        caloriesCalculator.setProfile(getProfile()!!)
        startSensor(caloriesCalculator, burnEvent)
    }

    private fun startTest() {
        MainScope().launch(Dispatchers.IO) {
            //startSensorTest(sensorEventListener)
        }
    }

    private fun startSensor(caloriesCalculator: CaloriesCalculator, burnEvent: BurnEvent) {
        sensorManager = context.getSystemService(Service.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        var allCalories = 0
        burnEvent.productsId.forEach {
            allCalories+=it.calory
        }
        var savedBurnedCalories = 0
        sensorManager.registerListener(object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val value = event!!.values[0].toInt()
                if (!caloriesCalculator.isRunning) {
                    caloriesCalculator.isRunning = true
                    caloriesCalculator.stepsInStart = value
                    savedBurnedCalories = burnEvent.caloriesBurned
                    println("saved isNOTRunning = ${savedBurnedCalories} calories")
                } else {
                    println("saved isRunning = ${savedBurnedCalories} calories")
                }
                val steps = value-caloriesCalculator.stepsInStart
                println("stepsCount = ${steps}")
                val caloriesBurned = caloriesCalculator.getBurnedCalories(steps) + savedBurnedCalories
                val caloriesLeft = allCalories - caloriesBurned
                println("caloriesLeft = ${caloriesLeft}")
                var status = Constants.BURN_EVENT_STATUS_IN_PROGRESS
                if (caloriesLeft<=0) {
                    println("done!")
                    status = Constants.BURN_EVENT_STATUS_DONE
                    sensorManager.unregisterListener(this)
                }

                val updatedBurnEvent = BurnEvent(
                    id = burnEvent.id,
                    productsId = burnEvent.productsId,
                    caloriesBurned = caloriesBurned,
                    eventStatus = status
                )
                println("burnEvent = ${burnEvent.toString()}")
                MainScope().launch(Dispatchers.IO) {
                    val result = repository.updateBurnEvent(updatedBurnEvent)
                    println("updateResult = ${result}")
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }, sensor, SensorManager.SENSOR_DELAY_UI)
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