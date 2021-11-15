package com.example.androidflier.ui.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.FlierApp
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.localdb.ManagerLocalStorage
import com.example.androidflier.ui.viewmodels.BaseShopViewModel

class FavoriteViewModel(context: Application): BaseShopViewModel(context) {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops

    init {
        startInitialize()
    }

    override fun startInitialize() {
        val flierApp = context as FlierApp
        _shops.value = localDb.getAllFavoriteShops()
    }

    fun refresh() {
        startInitialize()
    }
}