package com.ekochkov.burnthiscalories.services

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.data.entity.Profile
import com.ekochkov.burnthiscalories.util.CaloriesCalculator
import com.ekochkov.burnthiscalories.util.Constants

class BurnCaloriesService: Service() {

    private var isRunning = false
    private var stepsInStart = 0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Логируем старт метода
        println("!!! onStartCommand")
        val bundle = intent!!.getBundleExtra(Constants.BUNDLE_KEY)
        val profile = bundle!!.get(Constants.PROFILE_KEY) as Profile
        val burnList = bundle!!.get(Constants.BURN_LIST_KEY) as List<Product>
        //Запускаем новый поток, в котором будем выводить цифры в консоль с интервалом в секунду

        val caloriesCalculator = CaloriesCalculator()
        caloriesCalculator.setProfile(profile)
        //caloriesCalculator.setProducts(burnList)

        //burnList.forEach {
        //    println("${it.toString()}")
        //}
        startSensor(caloriesCalculator)
        //Executors.newSingleThreadExecutor().execute {
        //    repeat(100) {
        //        Thread.sleep(1000)
        //        println("!!!$it...")
        //        //println("!!!${profile.toString()}")
        //    }
        //    //По завершении работы останавливаем сервис
        //    stopSelf()
        //}
        //Режим для перезапуска сервиса
        return START_STICKY
    }

    private fun startSensor(caloriesCalculator: CaloriesCalculator) {
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.registerListener(object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val value = event!!.values[0].toInt()
                if (!isRunning) {
                    isRunning = true
                    stepsInStart = value
                }
                val steps = value-stepsInStart
                println("stepsCount = ${steps}")
                //caloriesCalculator.getCaloriesLeft(steps)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onCreate() {
        println("!!! onCreate")
    }

    override fun onDestroy() {
        println("!!! onDestroy")
    }
}