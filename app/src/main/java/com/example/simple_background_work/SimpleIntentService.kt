package com.example.simple_background_work

import android.app.IntentService
import android.content.Intent
import android.util.Log

const val INTENT_SERVICE_KEY: String = "intent_service_key"
private const val TAG: String = "SimpleIntentService"

class SimpleIntentService : IntentService(SimpleIntentService::class.java.simpleName) {

  override fun onCreate() {
    super.onCreate()
    Log.i(TAG, "SimpleIntentService create")
  }

  override fun onHandleIntent(intent: Intent?) {
    val action = intent?.getStringExtra(INTENT_SERVICE_KEY) ?: "bad data"
    Log.i(TAG, action)
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.i(TAG, "SimpleIntentService destroy")
  }
}
