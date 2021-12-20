package com.example.androidflier.util

import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.androidflier.NOTIFICATION_CHANNEL_ID
import com.example.androidflier.R

class ShopWorker(
    val context: Context,
    val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        Log.d("worker", "work")

        sendNotification()

        return Result.success()
    }

    companion object{
        const val SHOP_WORKER = "shop worker"
    }

    private fun sendNotification() {
        val notification = createNotification()
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, notification)
    }

    private fun createNotification(): Notification {
        return NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setTicker(context.resources.getString(R.string.new_stock_title_test))
            .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
            .setContentTitle("content title")
            .setContentText("cont text text")
            //.setContentIntent()
            .setAutoCancel(true)
            .build()
    }
}