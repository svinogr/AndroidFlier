package com.example.androidflier.repo.localdb

import android.content.Context
import com.example.androidflier.model.Shop

class ManagerLocalStorage(context: Context) : LocalDataStorageable {
    private val db = DataBaseHelper.getInstance(context)

    override fun getAllShops(): List<Shop> {
        return db.getAllShop()
    }

    override fun save(shop: Shop) {
        db.save(shop)
    }
}