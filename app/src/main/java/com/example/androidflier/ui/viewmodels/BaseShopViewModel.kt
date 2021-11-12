package com.example.androidflier.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.ShopRepository
import com.example.androidflier.repo.ShopRepositoryable
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.retrofit.RetrofitInst

abstract class BaseShopViewModel(open val context: Application): ViewModel() {
    var shopRepo: ShopRepositoryable
    var localDb: LocalDataStorageable

    init {
        val flierApp = context as FlierApp
        shopRepo = flierApp.shopRepo
        localDb = flierApp.localDb
    }

    abstract fun startInitialize()

    fun saveToLocalDb(shop: Shop) {
        shop.favoriteStatus = true
        localDb.save(shop)
    }

    fun deleteFromLocalDb(shop: Shop) {
        localDb.delete(shop)
    }
}