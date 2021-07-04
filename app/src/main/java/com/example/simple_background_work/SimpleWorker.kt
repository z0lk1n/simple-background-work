package com.example.simple_background_work

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

const val KEY_INPUT: String = "key_input"
private const val TAG: String = "SimpleWorker"
private const val KEY_OUTPUT: String = "key_output"

class SimpleWorker(appContext: Context, workerParams: WorkerParameters) :
  Worker(appContext, workerParams) {

  override fun doWork(): Result {
    return try {
      val msg = inputData.getString(KEY_INPUT) ?: return Result.failure()
      Log.i(TAG, msg)

      val outputData = createOutputData("Success get data")
      Result.success(outputData)
    } catch (e: Exception) {
      val outputData = createOutputData("failure process data")
      Result.failure(outputData)
    }
  }

  private fun createOutputData(firstData: String): Data {
    return Data.Builder()
      .putString(KEY_OUTPUT, firstData)
      .build()
  }
}
