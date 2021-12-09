package com.example.androidflier

import android.app.Application
import com.example.androidflier.repo.*
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.localdb.ManagerLocalStorage

class FlierApp() : Application() {
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
    }
}
