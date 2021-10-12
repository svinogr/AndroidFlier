package com.example.androidflier.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.androidflier.model.Shop
import com.example.androidflier.serverdb.Serverable
import com.example.androidflier.serverdb.ShopServer

class ShopsViewModel: ViewModel() {
    private var shopServer: Serverable<List<Shop>>

    init {
        shopServer = ShopServer()
    }

    fun getAllShops() = shopServer.getAll()



}