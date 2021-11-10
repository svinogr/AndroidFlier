package com.example.androidflier.ui.shopdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.BaseShopModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopViewModel(private val id: Long) : BaseShopModel() {
    private var _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop> = _shop

    init {
        Log.d("TAG", id.toString())
        startInitialize()
    }

    override fun startInitialize() {
        Log.d("TAG", "initialize")
        shopRepo.getShopWithStocks(id).enqueue(object : Callback<Shop> {
            override fun onFailure(call: Call<Shop>, t: Throwable) {
                Log.d("TAG", t.message.toString())
            }

            override fun onResponse(call: Call<Shop>, response: Response<Shop>) {
                _shop.value = response.body()
            }
        })

        //  _shop.value = shopRepo.getShopById(id)
    }
}
