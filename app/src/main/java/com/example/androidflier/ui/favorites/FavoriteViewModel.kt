package com.example.androidflier.ui.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.FlierApp
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.localdb.ManagerLocalStorage
import com.example.androidflier.ui.viewmodels.BaseShopViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteViewModel(context: Application): BaseShopViewModel(context) {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops

    override fun refreshData() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            try {
                _shops.postValue(localDb.getAllFavoriteShops())
            } catch (e: Exception) {
                _message.postValue(e.message)
                _shops.postValue(listOf())
            }
        }
    }
}