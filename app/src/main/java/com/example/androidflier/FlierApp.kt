package com.example.androidflier

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.androidflier.repo.*
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.localdb.ManagerLocalStorage
import com.example.androidflier.ui.profile.SettingsViewModel
import com.example.androidflier.util.ShopWorker

private const val SHARED_PREF = "shared pref"
const val NOTIFICATION_CHANNEL_ID = "channel id"
private const val FIRST_START_WORKER = "start worker"

class FlierApp() : Application() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var shopRepo: ShopRepositoryable
    lateinit var localDb: LocalDataStorageable
    lateinit var locationRepo: LocationReposable
    lateinit var tabRepo: TabReposable

    override fun onCreate() {
        super.onCreate()
        shopRepo = ShopRepository.getInstance()
        localDb = ManagerLocalStorage(this)
        locationRepo = LocationRepo.getInstance(this)
        tabRepo = TabRepo.getInstance()
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val isFirst = sharedPreferences.getBoolean(FIRST_START_WORKER, true)

        if(isFirst) {
            createShopWorker()
            val edit = sharedPreferences.edit()
            edit.putBoolean(FIRST_START_WORKER, false) // помечаем что что воркер запущен уже был
            edit.putBoolean(ShopWorker.SHOP_WORKER, true) // помечаем что воркер запущен
            edit.apply()
        }

        createChannel() // возможно стоит перенести в блок if выше
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createShopWorker() {
        val constraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequest
            .Builder(ShopWorker::class.java)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }
}
