package com.example.androidflier.ui.shopdetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.BaseShopViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopViewModel(private val id: Long, context: Application) : BaseShopViewModel(context) {
    private var _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop> = _shop

    fun getShop() {
        Log.d("ShopViewModel", "initialize")
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            shopRepo.getShopWithStocks(id).enqueue(object : Callback<Shop> {
                override fun onFailure(call: Call<Shop>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                }

                override fun onResponse(call: Call<Shop>, response: Response<Shop>) {
                    val shop = response.body()

                    if (shop != null) {
                        val shopIs = localDb.hasItInDd(id)

                        if (shopIs) {
                            shop.favoriteStatus = true
                        }

                        _shop.postValue(shop!!)
                    }
                }
            })
        }
    }

    fun changeFavoriteStatus() {
        GlobalScope.launch(Dispatchers.IO) {
            val deleteShop = _shop.value!!
            val deleteRow = localDb.delete(deleteShop)

            if (deleteRow > 0) {
                deleteShop.favoriteStatus = false
            } else {
                localDb.save(deleteShop)
                deleteShop.favoriteStatus = true
            }

            _shop.postValue(deleteShop)
        }
    }

    override fun refreshData() {
        getShop()
    }
}
