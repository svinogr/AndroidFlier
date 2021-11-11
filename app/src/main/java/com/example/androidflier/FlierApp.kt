package com.example.androidflier

import android.app.Application
import com.example.androidflier.repo.ShopRepository
import com.example.androidflier.repo.ShopRepositoryable
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.localdb.ManagerLocalStorage

class FlierApp() : Application() {
     lateinit var shopRepo: ShopRepositoryable
     lateinit var localDb: LocalDataStorageable

    override fun onCreate() {
        super.onCreate()
        shopRepo = ShopRepository.getInstance()
        localDb = ManagerLocalStorage(this)
    }
}
