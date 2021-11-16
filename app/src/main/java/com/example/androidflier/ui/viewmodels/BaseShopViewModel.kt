package com.example.androidflier.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.ShopRepository
import com.example.androidflier.repo.ShopRepositoryable
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.retrofit.RetrofitInst

abstract class BaseShopViewModel(val context: Context): ViewModel() {
    var shopRepo: ShopRepositoryable
    var localDb: LocalDataStorageable
    final val delayRefresh: Long = 700

    init {
        val flierApp = context.applicationContext as FlierApp
        shopRepo = flierApp.shopRepo
        localDb = flierApp.localDb
    }

    abstract fun refreshData()
}