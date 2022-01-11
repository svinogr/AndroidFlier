package com.example.androidflier.repo.localdb

import android.content.Context
import com.example.androidflier.model.Shop

class ManagerLocalStorage(context: Context) : LocalDataStorageable {
    private val db = DataBaseHelper.getInstance(context)

    override fun getAllFavoriteShops(): List<Shop> {
        return db.getAllFavoriteShop()
    }

    override fun getAllShops(): List<Shop> {
        return db.getAllShop()
    }

    override fun save(shop: Shop) {
        db.save(shop)
    }

    override fun update(shop: Shop) {
        db.update(shop)
    }

    override fun delete(shop: Shop): Int {
        return db.delete(shop)
    }

    override fun getShopById(id: Long): Shop? {
        return db.getShopById(id)
    }

    override fun hasItInDd(id: Long): Boolean {
        return db.hasItInDb(id)
    }
}