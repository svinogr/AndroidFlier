package com.example.androidflier.ui.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.ShopRepository
import com.example.androidflier.repo.ShopRepositoryable
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.retrofit.RetrofitInst

abstract class BaseShopViewModel(val context: Application): ViewModel() {
    var shopRepo: ShopRepositoryable
    var localDb: LocalDataStorageable
    val _message = MutableLiveData<String>()
    val message = _message
    final val delayRefresh: Long = 700

    init {
        val flierApp = context as FlierApp
        shopRepo = flierApp.shopRepo
        localDb = flierApp.localDb
    }

}