package com.example.simple_background_work

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.simple_background_work.SimpleJobIntentService.Companion.JOB_INTENT_SERVICE_KEY
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    startSimpleService()
    startSimpleForegroundService()
    startSimpleIntentService()
    startSimpleJobIntentService()
    startOneTimeSimpleWorker()
  }

  private fun startSimpleService() {
    Intent(this, SimpleService::class.java).also { intent ->
      startService(intent)
    }
  }

  private fun stopSimpleService() {
    Intent(this, SimpleService::class.java).also { intent ->
      stopService(intent)
    }
  }

  private fun startSimpleForegroundService() {
    Intent(this, SimpleForegroundService::class.java).also { intent ->
      ContextCompat.startForegroundService(this, intent)
    }
  }

  private fun startSimpleIntentService() {
    Intent(this, SimpleIntentService::class.java).also { intent ->
      intent.putExtra(INTENT_SERVICE_KEY, "SimpleIntentService do work")
      startService(intent)
    }
  }

  private fun startSimpleJobIntentService() {
    Intent(this, SimpleJobIntentService::class.java).also { intent ->
      intent.putExtra(JOB_INTENT_SERVICE_KEY, "SimpleJobIntentService do work")
      SimpleJobIntentService.enqueueWork(this, intent)
    }
  }

  private fun startOneTimeSimpleWorker() {
    val workRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<SimpleWorker>()
      .setInputData(workDataOf(KEY_INPUT to "SimpleWorker do work"))
      .setInitialDelay(10, TimeUnit.SECONDS)
      .setBackoffCriteria(
        BackoffPolicy.LINEAR, // The BackoffPolicy to use when increasing backoff time
        OneTimeWorkRequest.MIN_BACKOFF_MILLIS, // Time to wait before retrying the work in timeUnit units
        TimeUnit.MILLISECONDS) // The TimeUnit for backoffDelay
      .addTag("SingleSimpleWorkerTag")
      .build()

    WorkManager.getInstance(this)
      .enqueueUniqueWork(
        "SingleSimpleWorker", // A unique name which for this operation
        ExistingWorkPolicy.REPLACE, // An ExistingWorkPolicy
        workRequest) // The OneTimeWorkRequests to enqueue
  }

  private fun startPeriodicSimpleWorker() {
    val constraints = Constraints.Builder()
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .setRequiresCharging(true)
      .build()

    val workRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<SimpleWorker>(
      1, TimeUnit.HOURS, // repeatInterval (the period cycle)
      15, TimeUnit.MINUTES) // flexInterval
      .setConstraints(constraints)
      .build()

    WorkManager.getInstance(this)
      .enqueueUniquePeriodicWork(
        "PeriodicSimpleWorker", // A unique name which for this operation
        ExistingPeriodicWorkPolicy.KEEP, // An ExistingPeriodicWorkPolicy
        workRequest) // A PeriodicWorkRequest to enqueue
  }

  private fun cancelSimpleWorkers() {
    val workManager = WorkManager.getInstance(this)
    workManager.cancelAllWorkByTag("SingleSimpleWorkerTag")
    workManager.cancelUniqueWork("PeriodicSimpleWorker")
  }
}
