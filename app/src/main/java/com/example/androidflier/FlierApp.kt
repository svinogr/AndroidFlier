package com.example.androidflier

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.example.androidflier.repo.*
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.localdb.ManagerLocalStorage

private const val SHARED_PREF = "shared pref"
const val NOTIFICATION_CHANNEL_ID = "channel id"

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

        createChannel() // возможно стоит перенести
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
}
