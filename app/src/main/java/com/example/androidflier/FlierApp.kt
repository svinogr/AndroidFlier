package com.example.androidflier

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.androidflier.repo.*
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.localdb.ManagerLocalStorage
private const val SHARED_PREF = "shared pref"
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
    }
}
