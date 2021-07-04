package com.example.simple_background_work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

private const val TAG: String = "SimpleForegroundService"
private const val NOTIFICATION_CHANNEL_ID: String = "notification_channel_id"
private const val NOTIFICATION_CHANNEL_NAME: String = "Notification channel"
private const val START_FOREGROUND_ID: Int = 1

class SimpleForegroundService : Service() {

  override fun onCreate() {
    Log.i(TAG, "SimpleForegroundService creating")
    startForeground()
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    Log.i(TAG, "SimpleForegroundService starting")

    doWork()

    return START_STICKY
  }

  private fun startForeground() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_LOW
      )

      (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        .createNotificationChannel(channel)

      val notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
        .build()

      startForeground(START_FOREGROUND_ID, notification)
    }
  }

  private fun doWork() {
    Log.i(TAG, "SimpleForegroundService do work")

    try {
      Thread.sleep(10000)
    } catch (e: InterruptedException) {
      Thread.currentThread().interrupt()
    }

    stopService()
  }

  private fun stopService() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      stopForeground(true)
    }

    stopSelf()
  }

  override fun onBind(intent: Intent?): IBinder? = null

  override fun onDestroy() {
    Log.i(TAG, "SimpleForegroundService done")
  }
}
