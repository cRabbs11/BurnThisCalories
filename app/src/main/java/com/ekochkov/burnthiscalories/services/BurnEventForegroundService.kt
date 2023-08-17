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
import com.ekochkov.burnthiscalories.util.Constants
import javax.inject.Inject

class BurnEventForegroundService: Service(), SensorEventListener {

    @Inject
    lateinit var interactor: Interactor

    lateinit var sensorManager : SensorManager
    lateinit var sensor: Sensor
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        App.instance.dagger.inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(1, getNotification(Constants.BURN_EVENT_IS_RUNNING, "ккал осталось..."))
        startSensor()
        val burnEvent = intent?.getSerializableExtra(Constants.BURN_EVENT_KEY) as BurnEvent
        startCaloriesCalculator(burnEvent)
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

    private fun startCaloriesCalculator(burnEvent: BurnEvent?) {

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
        notificationManager.notify(1, getNotification(Constants.BURN_EVENT_IS_RUNNING, "ккал осталось...$value"))
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}