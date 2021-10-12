package com.example.androidflier.serverdb

import com.example.androidflier.model.Shop

class ShopServer : Serverable<List<Shop>> {
    override fun getAll(): List<Shop> {
        val shops = mutableListOf<Shop>()
        for (i in 0..20) {
            val shop = Shop(i.toLong(),  0.0, 0.0, "address $i", "description $i", "url $i", "img", "title $i" )
            shops += shop
        }

        return shops
    }

}