package com.example.maadjokes

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class NotificationWorker(val appContext: Context, parameters: WorkerParameters) :
    Worker(appContext, parameters) {
    override fun doWork(): Result {
        showNotification(appContext)
        return Result.success()
    }


}

val workerRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
    15,
    TimeUnit.MINUTES
).build()

//val testRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
//    .setInitialDelay(10, TimeUnit.SECONDS)
//    .build()

