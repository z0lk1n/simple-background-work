package com.example.simple_background_work

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class SimpleJobIntentService : JobIntentService() {

  override fun onCreate() {
    super.onCreate()
    Log.i(TAG, "SimpleJobIntentService create")
  }

  override fun onHandleWork(intent: Intent) {
    val action = intent.getStringExtra(JOB_INTENT_SERVICE_KEY) ?: "bad data"
    Log.i(TAG, action)
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.i(TAG, "SimpleJobIntentService destroy")
  }

  companion object {
    const val JOB_INTENT_SERVICE_KEY: String = "job_intent_service_key"

    private const val TAG: String = "SimpleJobIntentService"

    // Unique job ID for this service.
    private const val JOB_ID = 1000

    // Convenience method for enqueuing work in to this service.
    fun enqueueWork(context: Context, work: Intent) {
      enqueueWork(context, SimpleJobIntentService::class.java, JOB_ID, work)
    }
  }
}
