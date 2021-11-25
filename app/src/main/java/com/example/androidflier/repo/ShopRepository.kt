package com.example.androidflier.repo

import android.location.Location
import android.util.Log
import com.example.androidflier.model.Coord
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Tab
import com.example.androidflier.repo.retrofit.RetrofitInst
import retrofit2.Call

class ShopRepository private constructor() : ShopRepositoryable {
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

    override fun getAllShops(): Call<List<Shop>> {
        return shopApi.getAllShop()
    }

    override fun getAllShopsWithSearching(tab: Tab?, search: String): Call<List<Shop>> {
        Log.d("getAllShopsWithSearching", "getAllShopsWithSearching")
        var tabSearchText = ""
        if (tab != null) tabSearchText = tab.title

        val searchText = search.trim()

        return shopApi.getAllShopWithSearching(tabSearchText, searchText)
    }

    override fun getShopWithStocks(shopId: Long): Call<Shop> {
        return shopApi.getShopWithStocks(shopId)
    }

    override fun searchShops(query: String): Call<List<Shop>> {
        return shopApi.search(query)
    }

    override fun getAllNearestShopsWithSearching(
        location: Location,
        tab: Tab?,
        search: String
    ): Call<List<Shop>> {
        Log.d("getAllNearestShops", "${location.latitude} ${location.longitude}")
        //val coord = Coord(-57.5, -25.3)
        val coord = Coord(location.longitude, location.latitude)

        var tabSearchText = ""
        if (tab != null) tabSearchText = tab.title

        val searchText = search.trim()

        return shopApi.getAllNearestShopWithSearching(
            coord.lng,
            coord.lat,
            tabSearchText,
            searchText
        )
    }
}