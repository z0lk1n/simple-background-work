package com.example.simple_background_work

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

private const val TAG: String = "SimpleService"

class SimpleService : Service() {

  override fun onCreate() {
    Log.i(TAG, "SimpleService creating")
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    Log.i(TAG, "SimpleService starting")

    doWork()

    // If we get killed, after returning from here, restart
    return START_STICKY
  }

  private fun doWork() {
    Log.i(TAG, "SimpleService do work")
    stopSelf()
  }

  override fun onBind(intent: Intent): IBinder? {
    // We don't provide binding, so return null
    return null
  }

  override fun onDestroy() {
    Log.i(TAG, "SimpleService done")
  }
}
