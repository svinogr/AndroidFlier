package com.example.androidflier.repo

import android.location.Location
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Tab
import retrofit2.Call

interface ShopRepositoryable {

    fun getAllShops(): Call<List<Shop>>

    fun getAllShopsWithSearching(tab: Tab?, search: String, from: String = "0"): Call<List<Shop>>

    fun getShopWithStocks(shopId: Long): Call<Shop>

    fun searchShops(query: String): Call<List<Shop>>

    fun getAllNearestShopsWithSearching(location: Location, tab: Tab?, search: String, from: String = "0"): Call<List<Shop>>
}