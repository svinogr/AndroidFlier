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
                val shop = response.body()
                val localShop: List<Shop> = localDb.getAllFavoriteShops()

                if (localShop.contains(shop)) shop?.favoriteStatus = true
                Log.d("TAG", shop?.favoriteStatus.toString())
                if (shop != null) {
                    _shop.value = shop!!
                }
            }
        })

    }

}
