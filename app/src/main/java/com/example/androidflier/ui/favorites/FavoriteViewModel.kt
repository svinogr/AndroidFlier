package com.example.androidflier.ui.favorites

import android.app.Application
import com.example.androidflier.model.Tab
import com.example.androidflier.ui.viewmodels.BaseShopsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteViewModel(context: Application): BaseShopsViewModel(context) {

     fun refreshData() {
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

    override fun loadMore(selectedTab: Tab?, searchText: String) {

    }

    override fun refreshDataSearch(selectedTab: Tab?, searchText: String) {

    }

    override fun allShops(tab: Tab?, searchText: String) {

    }
}