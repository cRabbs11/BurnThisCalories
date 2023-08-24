package com.ekochkov.burnthiscalories.services

import android.app.*
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.ekochkov.burnthiscalories.App

import com.ekochkov.burnthiscalories.R
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.domain.Interactor
import com.ekochkov.burnthiscalories.util.CaloriesCalculator
import com.ekochkov.burnthiscalories.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BurnEventForegroundService: Service(), SensorEventListener {

    companion object {
        private var serviceIsRunningFlow = MutableStateFlow(false)

        fun isServiceRunningFlow() = serviceIsRunningFlow
    }

    @Inject
    lateinit var interactor: Interactor

    lateinit var caloriesCalculator: CaloriesCalculator
    lateinit var sensorManager : SensorManager
    lateinit var sensor: Sensor
    lateinit var notificationManager: NotificationManager
    private lateinit var currentBurnEvent: BurnEvent
    private var startedCalories = 0
    private var startedSteps = 0
    private var sensorIsRunning = false

    override fun onCreate() {
        super.onCreate()
        App.instance.dagger.inject(this)
        GlobalScope.launch {
            serviceIsRunningFlow.emit(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        GlobalScope.launch(Dispatchers.Default) {
            val burnEventId = intent?.getIntExtra(Constants.BURN_EVENT_ID_KEY, -1) as Int
            burnEventId.let {
                currentBurnEvent = interactor.getBurnEvent(burnEventId)!!
                createNotificationChannel()
                startForeground(1, getNotification(Constants.BURN_EVENT_IS_RUNNING, "ккал осталось..."))
                startCaloriesCalculator()
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                Constants.BURN_EVENT_SERVICE_CHANNEL_ID,
                Constants.BURN_EVENT_SERVICE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }

    private fun startSensor() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun stopSensor() {
        sensorManager?.let {
            it.unregisterListener(this)
        }
    }

    private fun startCaloriesCalculator() {
        GlobalScope.launch(Dispatchers.Default) {
            val profile = interactor.getProfile()
            if (profile!=null) {
                caloriesCalculator = CaloriesCalculator.Builder()
                    .setBurnEvent(currentBurnEvent)
                    .setProfile(profile)
                    .build()
                startSensor()
            } else {
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSensor()
        GlobalScope.launch {
            serviceIsRunningFlow.emit(false)
        }
    }

    private fun saveBurnEvent(status: Int, caloriesBurned: Int) {
        val updatedBurnEvent = BurnEvent(
            id = currentBurnEvent.id,
            productsId = currentBurnEvent.productsId,
            caloriesBurned = caloriesBurned,
            eventStatus = status
        )
        GlobalScope.launch(Dispatchers.IO) {
            interactor.updateBurnEvent(updatedBurnEvent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotification(title: String, text: String): Notification {
        val notification: Notification = Notification.Builder(this, Constants.BURN_EVENT_SERVICE_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_baseline_fitness_center_24)
            .build()
        return notification
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {

        val value = event!!.values[0].toInt()
        if (!sensorIsRunning) {
            sensorIsRunning = true
            startedCalories = currentBurnEvent.caloriesBurned
            startedSteps = value
        }
        val caloriesBurned = caloriesCalculator.fromStepsToCalory(value-startedSteps) + startedCalories
        val caloriesLeft = caloriesCalculator.getAllCalories()-caloriesBurned
            if (caloriesLeft>=0) {
                notificationManager.notify(1, getNotification(Constants.BURN_EVENT_IS_RUNNING, "ккал осталось...$caloriesLeft"))
                saveBurnEvent(Constants.BURN_EVENT_STATUS_IN_PROGRESS, caloriesBurned)
            } else {
                notificationManager.notify(1, getNotification(Constants.BURN_EVENT_IS_RUNNING, "событие закончено!"))
                saveBurnEvent(Constants.BURN_EVENT_STATUS_DONE, caloriesBurned)
                stopSelf()
            }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}