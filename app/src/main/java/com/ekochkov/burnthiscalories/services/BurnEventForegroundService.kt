package com.ekochkov.burnthiscalories.services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi

import com.ekochkov.burnthiscalories.R
import com.ekochkov.burnthiscalories.util.Constants

class BurnEventForegroundService: Service() {

    lateinit var notificationManager: NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(1, getNotification(Constants.BURN_EVENT_IS_RUNNING, "ккал осталось..."))
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotification(title: String, text: String): Notification {
        val notification: Notification = Notification.Builder(this, Constants.BURN_EVENT_SERVICE_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_baseline_fitness_center_24)
            .build()
        return notification
    }
}