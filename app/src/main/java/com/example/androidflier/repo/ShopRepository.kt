package com.example.androidflier.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Stock

class ShopRepository private constructor() {

    companion object {
        private lateinit var instance: ShopRepository

        init {
            instance = ShopRepository()
        }

        fun getInstance(): ShopRepository {

            return instance ?: ShopRepository()
        }
    }

    fun getAll(): String {
        return ""
    }

    fun getAllTestNearestShop(): List<Shop> {
        val shopsList = mutableListOf<Shop>()
        for (i in 0..20) {
            val shop = Shop(
                i.toLong(),
                0.0,
                0.0,
                "address $i",
                "description $i",
                "url $i",
                "img",
                "title $i",
                mutableListOf()
            )

            shopsList += shop
        }

        return shopsList
    }

    fun getAllShopsByTitle(title: String) {

    }

    fun getShopById(id: Long): Shop {
        // TODO
        val stocks = mutableListOf(Stock(1, "title1", "descr1"), Stock(2, "title2", "descr2"))
        val shop = Shop(id, 10.00, 10.00, "adress", "descr", "www", "img", "title",  stocks)
        return shop
    }
}