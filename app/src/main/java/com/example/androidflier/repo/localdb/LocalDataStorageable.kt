package com.example.androidflier.repo.localdb

import com.example.androidflier.model.Shop

interface LocalDataStorageable {
    fun getAllShops(): List<Shop>
    fun save(shop: Shop)
}