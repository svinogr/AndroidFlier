package com.example.androidflier.util

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.androidflier.FlierApp
import com.example.androidflier.NOTIFICATION_CHANNEL_ID
import com.example.androidflier.R
import com.example.androidflier.model.SettingsSearch
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.LocationReposable
import com.example.androidflier.repo.ShopRepositoryable
import com.example.androidflier.repo.localdb.LocalDataStorageable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopWorker(
    val context: Context,
    val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    private var location: LocationReposable
    private var shopRepo: ShopRepositoryable
    private var localDB: LocalDataStorageable
    private var preff: SharedPreferences

    init {
        val flierApp = context.applicationContext as FlierApp
        shopRepo = flierApp.shopRepo
        localDB = flierApp.localDb
        location = flierApp.locationRepo
        preff = flierApp.sharedPreferences
    }

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        Log.d("worker", "work")
     /*   val listTagFromSharedPref = SettingsSearch.getListTagFromSharedPref(preff)
        location.getLocateClient().lastLocation.addOnCompleteListener { location ->
            val loc = location.result
            shopRepo.getAllNearestShopsWithSearching(loc, null, listTagFromSharedPref[0])
                .enqueue(object : Callback<List<Shop>> {
                    override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                        Log.d("ShopWorker", "fail: $t.toString()")

                    }

                    override fun onResponse(
                        call: Call<List<Shop>>,
                        response: Response<List<Shop>>
                    ) {
                        Log.d("ShopWorker", "resp: $response.toString()")
                        if (response.code() != 200) {
                         *//*   message.postValue(response.message())
                            _shops.postValue(listOf())*//*
                        } else {
                    *//*        _shops.postValue(response.body())*//*
                        }
                    }
                })
        }*/

        sendNotification()

        return Result.success()
    }

    companion object {
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

    override fun onStopped() {
        super.onStopped()
        Log.d("worker", "stop")
    }
}