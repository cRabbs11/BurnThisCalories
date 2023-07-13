package com.ekochkov.burnthiscalories.util
import android.app.Service
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.ekochkov.burnthiscalories.data.CaloriesRepository
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CaloriesCalculator(private val context: Context, private val repository: CaloriesRepository): SensorEventListener {

    private lateinit var profile : Profile

    var isRunning = false
    var stepsInStart = 0

    private var stepDistance = 0.0
    private var weight = 0.0
    private val SM_TO_KM_KOEF = 10000 //коэф для пересчета расстояния из см в км
    private val STEP_LENGTH_KOEF = 0.41

    private lateinit var sensor: Sensor
    private var sensorManager: SensorManager? = null
    private var savedBurnedCalories = 0
    private lateinit var burnEvent: BurnEvent
    private var allCalories = 0


    fun setProfile(profile: Profile) {
        this.profile = profile
        stepDistance = profile.height.toDouble()*STEP_LENGTH_KOEF //считаем длину шага в см: 41% oт pocтa
        weight = profile.weight.toDouble()
    }

    fun getBurnedCalories(steps: Int): Int {
        //количесвто сожженных килокалорий нв километр: 0,5 х вec чeлoвeka (kг) х paccтoяниe (km) = coжжeнныe Kkaл

        val caloriesBurned = weight*0.5*(stepDistance*steps)/SM_TO_KM_KOEF
        return caloriesBurned.toInt()
    }

    fun startCalculator(burnEvent: BurnEvent) {
        this.burnEvent = burnEvent
        sensorManager = context.getSystemService(Service.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        allCalories = 0
        this.burnEvent.productsId.forEach {
            allCalories+=it.calory
        }

        savedBurnedCalories = 0
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopCalculator() {
        sensorManager?.let{
            it.unregisterListener(this)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        MainScope().launch(Dispatchers.IO) {
        val value = event!!.values[0].toInt()
        if (!isRunning) {
            isRunning = true
            stepsInStart = value
            savedBurnedCalories = burnEvent.caloriesBurned
            println("saved isNOTRunning = ${savedBurnedCalories} calories")
        } else {
            println("saved isRunning = ${savedBurnedCalories} calories")
        }
        val steps = value-stepsInStart
        println("stepsCount = ${steps}")
        val caloriesBurned = getBurnedCalories(steps) + savedBurnedCalories
        val caloriesLeft = allCalories - caloriesBurned
        println("caloriesLeft = ${caloriesLeft}")
        var status = Constants.BURN_EVENT_STATUS_IN_PROGRESS
        if (caloriesLeft<=0) {
            println("done!")
            status = Constants.BURN_EVENT_STATUS_DONE
            stopCalculator()
            isRunning = false
        }

        val updatedBurnEvent = BurnEvent(
            id = burnEvent.id,
            productsId = burnEvent.productsId,
            caloriesBurned = caloriesBurned,
            eventStatus = status
        )
        println("burnEvent = ${burnEvent.toString()}")
            val result = repository.updateBurnEvent(updatedBurnEvent)
            println("updateResult = ${result}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}