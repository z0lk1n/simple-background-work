package com.example.simple_background_work

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.simple_background_work.SimpleJobIntentService.Companion.JOB_INTENT_SERVICE_KEY
import com.example.simple_background_work.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  private var simpleBoundService: SimpleBoundService? = null
  private var isBound = false

  // Defines callbacks for service binding, passed to bindService()
  private val serviceConnection = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
      // We've bound to SimpleBoundService, cast the IBinder and get SimpleBoundService instance
      simpleBoundService = (service as SimpleBoundService.LocalBinder).getService()
      isBound = true
    }

    override fun onServiceDisconnected(name: ComponentName) {
      simpleBoundService = null
      isBound = false
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    init()
  }

  private fun init() {
    binding.btnStartSimpleService.setOnClickListener { startSimpleService() }
    binding.btnStopSimpleService.setOnClickListener { stopSimpleService() }
    binding.btnStartForegroundService.setOnClickListener { startForegroundService() }
    binding.btnStopForegroundService.setOnClickListener { stopForegroundService() }
    binding.btnSendActionBoundService.setOnClickListener { sendActionBoundService() }
    binding.btnStartIntentService.setOnClickListener { startIntentService() }
    binding.btnStartJobIntentService.setOnClickListener { startJobIntentService() }
    binding.btnStartOneTimeWorker.setOnClickListener { startOneTimeWorker() }
    binding.btnStartPeriodicWorker.setOnClickListener { startPeriodicWorker() }
    binding.btnCancelAllWorkers.setOnClickListener { cancelWorkers() }
  }

  override fun onStart() {
    super.onStart()
    bindBoundService()
  }

  private fun bindBoundService() {
    // Bind to SimpleBoundService
    Intent(this, SimpleBoundService::class.java).also { intent ->
      bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
  }

  override fun onStop() {
    super.onStop()
    unbindBoundService()
  }

  private fun unbindBoundService() {
    if (isBound) {
      unbindService(serviceConnection)
      isBound = false
    }
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

  private fun startForegroundService() {
    Intent(this, SimpleForegroundService::class.java).also { intent ->
      ContextCompat.startForegroundService(this, intent)
    }
  }

  private fun stopForegroundService() {
    Intent(this, SimpleForegroundService::class.java).also { intent ->
      stopService(intent)
    }
  }

  private fun sendActionBoundService() {
    if (!isBound) return
    // Call a method from the SimpleBoundService.
    // However, if this call were something that might hang, then this request should
    // occur in a separate thread to avoid slowing down the activity performance.
    simpleBoundService?.sendActionMessage("SimpleBoundService do work")
  }

  private fun startIntentService() {
    Intent(this, SimpleIntentService::class.java).also { intent ->
      intent.putExtra(INTENT_SERVICE_KEY, "SimpleIntentService do work")
      startService(intent)
    }
  }

  private fun startJobIntentService() {
    Intent(this, SimpleJobIntentService::class.java).also { intent ->
      intent.putExtra(JOB_INTENT_SERVICE_KEY, "SimpleJobIntentService do work")
      SimpleJobIntentService.enqueueWork(this, intent)
    }
  }

  private fun startOneTimeWorker() {
    val workRequest = OneTimeWorkRequestBuilder<SimpleWorker>()
      .setInputData(workDataOf(KEY_INPUT to "OneTimeSimpleWorker do work"))
      .setInitialDelay(1, TimeUnit.MINUTES)
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

  private fun startPeriodicWorker() {
    val constraints = Constraints.Builder()
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .setRequiresCharging(true)
      .build()

    val workRequest = PeriodicWorkRequestBuilder<SimpleWorker>(
      1, TimeUnit.HOURS, // repeatInterval (the period cycle)
      15, TimeUnit.MINUTES) // flexInterval
      .setConstraints(constraints)
      .build()

    WorkManager.getInstance(this)
      .enqueueUniquePeriodicWork(
        "PeriodicSimpleWorkerName", // A unique name which for this operation
        ExistingPeriodicWorkPolicy.REPLACE, // An ExistingPeriodicWorkPolicy
        workRequest) // A PeriodicWorkRequest to enqueue
  }

  private fun cancelWorkers() {
    WorkManager.getInstance(this).run {
      cancelAllWorkByTag("SingleSimpleWorkerTag")
      cancelUniqueWork("PeriodicSimpleWorkerName")
    }
  }
}
