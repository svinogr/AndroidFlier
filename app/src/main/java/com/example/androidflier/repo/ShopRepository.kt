package com.example.androidflier.repo

import com.example.androidflier.model.Coord
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.retrofit.RetrofitInst
import retrofit2.Call

class ShopRepository private constructor() {
    private lateinit var shopApi: ShopApi

    companion object {
        private lateinit var instance: ShopRepository

        init {
            instance = ShopRepository()
            instance.shopApi = RetrofitInst.getInstance().shopApi
        }

        fun getInstance(): ShopRepository {
            return instance ?: ShopRepository()
        }
    }

    fun getAllShops(): Call<List<Shop>> {
        return shopApi.getAllShop()
    }

    fun getAllNearestShops(): Call<List<Shop>> {
        //TODO заменить на пполучение координат
        val coord = Coord(-57.5, -25.3)
       //TODO
        return shopApi.getAllNearestShop(coord.lng, coord.lat)
    }

    fun getShopWithStocks(shopId: Long): Call<Shop> {
        return shopApi.getShopWithStocks(shopId)
    }
}