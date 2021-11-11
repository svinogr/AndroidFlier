package com.example.androidflier.repo

import com.example.androidflier.model.Coord
import com.example.androidflier.model.Shop
import retrofit2.Call

interface ShopRepositoryable {

    fun getAllShops(): Call<List<Shop>>

    fun getAllNearestShops(): Call<List<Shop>>

    fun getShopWithStocks(shopId: Long): Call<Shop>
}