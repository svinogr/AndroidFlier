package com.example.androidflier.repo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Stock

class ShopRepository private constructor() {
  lateinit var shopApi: ShopApi

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

/*
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
*/

    fun getAllShopsByTitle(title: String) {

    }

 /*   fun getShopById(id: Long): Shop {
        // TODO
        val stocks = mutableListOf(Stock(1, "акция 1 в три строки или больше или в пять даже и может даже в шестнадцать", "описание 1", 500.00, 600.00), Stock(2, "акция 2 в три строки или больше или в пять даже  и может даже в шестнадцать", "описание 2", 500.00, 600.00))
        val shop = Shop(id, 10.00, 10.00, "adress", "descr", "www", "img", "название магаза",  stocks)
        return shop
    }*/
}