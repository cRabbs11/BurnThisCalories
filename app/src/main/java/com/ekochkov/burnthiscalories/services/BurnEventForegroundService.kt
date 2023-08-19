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
import kotlinx.coroutines.launch
import javax.inject.Inject

class BurnEventForegroundService: Service(), SensorEventListener {

    @Inject
    lateinit var interactor: Interactor

    lateinit var caloriesCalculator: CaloriesCalculator
    lateinit var sensorManager : SensorManager
    lateinit var sensor: Sensor
    lateinit var notificationManager: NotificationManager
    private lateinit var currentBurnEvent: BurnEvent

    override fun onCreate() {
        super.onCreate()
        App.instance.dagger.inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(1, getNotification(Constants.BURN_EVENT_IS_RUNNING, "ккал осталось..."))
        //TODO проверить и поправить CaloriesCalculator
        startCaloriesCalculator()
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
        println("startCaloriesCalculator...")
        GlobalScope.launch(Dispatchers.Default) {
            println("globalScope...")
            val burnEvent = interactor.getBurnEventInProgress()
            val profile = interactor.getProfile()
            if (burnEvent!=null && profile!=null) {
                currentBurnEvent = burnEvent
                println("startBuilder...")
                caloriesCalculator = CaloriesCalculator.Builder()
                    .setBurnEvent(burnEvent)
                    .setProfile(profile)
                    .build()
                startSensor()
            } else {
                println("notStartBuilder...")
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSensor()
    }

    private fun saveBurnEvent() {
        println("done!")
        val status = Constants.BURN_EVENT_STATUS_DONE
        caloriesCalculator.stopRunning()
        val updatedBurnEvent = BurnEvent(
            id = currentBurnEvent.id,
            productsId = currentBurnEvent.productsId,
            caloriesBurned = caloriesCalculator.getAllCalories(),
            eventStatus = status
        )
        GlobalScope.launch(Dispatchers.IO) {
            val result = interactor.updateBurnEvent(updatedBurnEvent)
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
        println("onSensorChanged...")
        val value = event!!.values[0].toInt()
        if (!caloriesCalculator.isRunning()) {
            caloriesCalculator.setStartedStep(value)
        }
        val caloriesLeft = caloriesCalculator.getCaloriesLeft(value)

        if (caloriesLeft>=0) {
            notificationManager.notify(1, getNotification(Constants.BURN_EVENT_IS_RUNNING, "ккал осталось...$caloriesLeft"))
        } else {
            notificationManager.notify(1, getNotification(Constants.BURN_EVENT_IS_RUNNING, "событие закончено!"))
            saveBurnEvent()
            stopSelf()
            //GlobalScope.launch {
            //    delay(10000)
            //}
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}