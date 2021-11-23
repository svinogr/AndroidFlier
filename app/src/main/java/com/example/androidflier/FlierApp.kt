package com.example.androidflier

import android.app.Application
import com.example.androidflier.repo.LocationRepo
import com.example.androidflier.repo.LocationReposable
import com.example.androidflier.repo.ShopRepository
import com.example.androidflier.repo.ShopRepositoryable
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.localdb.ManagerLocalStorage
import com.google.android.gms.location.FusedLocationProviderClient

class FlierApp() : Application() {
     lateinit var shopRepo: ShopRepositoryable
     lateinit var localDb: LocalDataStorageable
     lateinit var locationRepo: LocationReposable

    override fun onCreate() {
        super.onCreate()
        shopRepo = ShopRepository.getInstance()
        localDb = ManagerLocalStorage(this)
        locationRepo = LocationRepo.getInstance(this)
    }
}
