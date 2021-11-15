package com.example.androidflier.ui.shopdetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.BaseShopViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopViewModel(private val id: Long, context: Application) : BaseShopViewModel(context) {
    private var _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop> = _shop

    init {
        Log.d("ShopViewModel", id.toString())
        startInitialize()
    }

    override fun startInitialize() {
        Log.d("ShopViewModel", "initialize")
        shopRepo.getShopWithStocks(id).enqueue(object : Callback<Shop> {
            override fun onFailure(call: Call<Shop>, t: Throwable) {
                Log.d("TAG", t.message.toString())
            }

            override fun onResponse(call: Call<Shop>, response: Response<Shop>) {
                val shop = response.body()

                if (shop != null) {
                    val localShop: Shop? = localDb.getShopById(shop.id)

                    if (localShop != null) {
                        shop?.favoriteStatus = true
                        Log.d("ShopViewModel", shop?.favoriteStatus.toString())
                    }
                    _shop.value = shop!!
                }
            }
        })

    }

    fun changeFavoriteStatus() {
        val deleteShop = _shop.value!!
        val deleteRow = localDb.delete(deleteShop)
        if (deleteRow > 0) {
            deleteShop.favoriteStatus = false
        } else {
            localDb.save(deleteShop)
            deleteShop.favoriteStatus = true
        }
        _shop.value = deleteShop
    }

    fun deleteFromLocalDb(shop: Shop) {
        localDb.delete(shop)
    }

    fun saveToLocalDb(shop: Shop) {
        localDb.save(shop)
    }
}
