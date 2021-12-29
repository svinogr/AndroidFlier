package com.example.androidflier.util

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Looper
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
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopWorker(
    val context: Context,
    val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    private lateinit var locationCallback: LocationCallback
    private var locationRepo: LocationReposable
    private var shopRepo: ShopRepositoryable
    private var localDB: LocalDataStorageable
    private var preff: SharedPreferences

    init {
        val flierApp = context.applicationContext as FlierApp
        shopRepo = flierApp.shopRepo
        localDB = flierApp.localDb
        locationRepo = flierApp.locationRepo
        preff = flierApp.sharedPreferences
    }

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        Log.d("worker", "work")


        //TODO получаем список магазинов
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

         locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
             /*   for (location in locationResult.locations) {
                    Log.d("worker", "callback ${location.latitude} ${location.longitude}")
                    magic()
                }*/

                magic(locationResult.locations[0])


                locationRepo.getLocateClient().removeLocationUpdates(locationCallback)
            }
        }

        locationRepo.getLocateClient()
            .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

     /*   location.getLocateClient().lastLocation.addOnCompleteListener { location ->
            val loc = location.result
            Log.d("work loc", location.result.toString())
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
                        if (response.code() == 200) {
                            val listMessages = response.body() ?: return
                            //TODO переделать на что нить побыстрее
                            listMessages.forEach {
                                if (!allLocalShops.contains(it)) {
                                    summaryShopList.add(it)
                                }
                            }

                            sendNotification(summaryShopList)
                        }
                    }
                })
        }*/



        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun magic(location: Location?) {

        val listTagFromSharedPref = SettingsSearch.getListTagFromSharedPref(preff)
        val testSearchValue = listTagFromSharedPref[0]
        val allLocalShops = localDB.getAllShops()
        val summaryShopList = mutableListOf<Shop>()
        locationRepo.getLocateClient().lastLocation.addOnCompleteListener { location ->
            val loc = location.result
            Log.d("work loc", location.result.toString())
            shopRepo.getAllNearestShopsWithSearching(loc, null, " ")
                .enqueue(object : Callback<List<Shop>> {
                    override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                        Log.d("ShopWorker", "fail: $t.toString()")

                    }

                    override fun onResponse(
                        call: Call<List<Shop>>,
                        response: Response<List<Shop>>
                    ) {
                        Log.d("ShopWorker", "resp: $response.toString()")
                        if (response.code() == 200) {
                            val listMessages = response.body() ?: return
                            //TODO переделать на что нить побыстрее
                            listMessages.forEach {
                                if (!allLocalShops.contains(it)) {
                                    summaryShopList.add(it)
                                }
                            }

                            sendNotification(summaryShopList)
                        }
                    }
                })
        }
    }


    //TODO проверяем есть ли в базе они. Если кокого то нет. то пишем его в базу и оповещаем о нем
    //TODO сравниваем колво акции по полученым магазам. При изменении кол-ва (и ID)  уведомляем и пшем новое значение колва в базу
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


    companion object {
        const val SHOP_WORKER = "shop worker"
    }

    private fun sendNotification(list: List<Shop>) {
        val notification = createNotification(list.size)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, notification)
    }

    private fun createNotification(size: Int): Notification {
        return NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setTicker(context.resources.getString(R.string.new_stock_title_test))
            .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
            .setContentTitle("Рядом новые магазины")
            .setContentText("$size магазинов")
            //.setContentIntent()
            .setAutoCancel(true)
            .build()
    }

    override fun onStopped() {
        Log.d("worker", "stop")
        locationRepo.getLocateClient().removeLocationUpdates(locationCallback)
        super.onStopped()
    }
}