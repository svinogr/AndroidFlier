package com.example.androidflier.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Looper
import android.service.autofill.LuhnChecksumValidator
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

class ShopNewWorker(
    val context: Context,
    val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    private lateinit var allLocalShops: List<Shop>
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

        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun magic(location: Location?) {
// получаем список тэгов для поиска из установленных пользователем настроек
        val listTagFromSharedPref = SettingsSearch.getListTagFromSharedPref(preff)
        // для теста берем один тэк
        val testSearchValue = listTagFromSharedPref[0]
        // получаем все магазы из локальной базы
        //val allLocalShops = localDB.getAllShops()
        //создаем сумарный список
        // val summaryShopList = mutableListOf<Shop>()
        //
        locationRepo.getLocateClient().lastLocation.addOnCompleteListener { location ->
            val loc = location.result
            Log.d("work loc", location.result.toString())
            shopRepo.getAllNearestShopsWithSearching(loc, null, " ")
                .enqueue(object : Callback<List<Shop>> {
                    //если ошибка получения данныйх то код здесь
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

                            setNotification(listMessages)
                        }
                    }
                })
        }
    }

    companion object {
        const val SHOP_WORKER = "shop worker"
    }

    private fun setNotification(list: List<Shop>) {
        if (list.size < 0) return

        allLocalShops = localDB.getAllShops()

        createNotificationNewShops(list)
        createNotificationNewStocks(list)
    }

    private fun createNotificationNewStocks(list: List<Shop>) {
        val shopsWithNewStocks = checkStocksInLocalDB(list)
        if(shopsWithNewStocks.isEmpty()) return

        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setTicker(context.resources.getString(R.string.new_stock_title_test))
            .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
            .setContentTitle("Новые скидки")
            .setContentText("${shopsWithNewStocks.size} магазинов обновили скидки")
            //.setContentIntent()
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, notification)
    }

    private fun checkStocksInLocalDB(list: List<Shop>): List<Shop> {
        val summaryShopList = mutableListOf<Shop>()

        for (i in 0..list.size - 1) {
           for(j in 0..allLocalShops.size - 1) {
               // проверяем чтобы количество stock с сервера для магазина было не равно записаному ранее в базу
               if(list[i].id == allLocalShops[j].id && list[i].stocks.size != allLocalShops[j].countStock ) {
                   summaryShopList.add(list[i])
               //TODO localDB.update(list[i])  раскоментить в бою
               }
           }
        }

        return summaryShopList
    }

    private fun createNotificationNewShops(list: List<Shop>) {
        val newShops = checkShopsInLocalDB(list)
        if(newShops.isEmpty()) return

        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setTicker(context.resources.getString(R.string.new_stock_title_test))
            .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
            .setContentTitle("Рядом новые магазины")
            .setContentText("${newShops.size} магазинов")
            //.setContentIntent()
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, notification)
    }


    //  возвращает только новые магазины ненаходящиеся в базе
    private fun checkShopsInLocalDB(list: List<Shop>): List<Shop> {
        val summaryShopList = mutableListOf<Shop>()

        list.forEach {
            if (!allLocalShops.contains(it)) {
                summaryShopList.add(it)
                // TODO   localDB.save(list[i])  раскоментить в бою
            }
        }

        return summaryShopList
    }

    override fun onStopped() {
        Log.d("worker", "stop")
        locationRepo.getLocateClient().removeLocationUpdates(locationCallback)
        super.onStopped()
    }
}