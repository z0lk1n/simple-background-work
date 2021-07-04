package com.example.simple_background_work

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

private const val TAG: String = "SimpleBoundService"

class SimpleBoundService : Service() {

  // Binder given to clients
  private val binder = LocalBinder()

  override fun onCreate() {
    Log.i(TAG, "SimpleBoundService create")
  }

  override fun onBind(intent: Intent): IBinder {
    Log.i(TAG, "SimpleBoundService bind")
    return binder
  }

  override fun onUnbind(intent: Intent?): Boolean {
    Log.i(TAG, "SimpleBoundService unbind")
    return super.onUnbind(intent)
  }

  override fun onRebind(intent: Intent?) {
    super.onRebind(intent)
    Log.i(TAG, "SimpleBoundService rebind")
  }

  override fun onDestroy() {
    Log.i(TAG, "SimpleBoundService destroy")
  }

  fun sendActionMessage(msg: String) {
    Log.i(TAG, msg)
  }

  /**
   * Class used for the client Binder. Because we know this service always
   * runs in the same process as its clients, we don't need to deal with IPC.
   */
  inner class LocalBinder : Binder() {
    // Return this instance of SimpleBoundService so clients can call public methods
    fun getService(): SimpleBoundService = this@SimpleBoundService
  }
}
