package com.example.androidflier.repo.localdb

import com.example.androidflier.model.Shop

interface LocalDataStorageable {
    fun getAllFavoriteShops(): List<Shop>
    fun save(shop: Shop)
    fun delete(shop: Shop): Int
    fun getShopById(id: Long): Shop?
}