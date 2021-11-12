package com.example.androidflier.ui.nearest

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.BaseShopViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NearestListShopsViewModel(context: Application) : BaseShopViewModel(context) {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops

    init {
        startInitialize()
    }

    fun allNearestShops() {
        shopRepo.getAllNearestShops().enqueue(object : Callback<List<Shop>> {
            override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                Log.d("TAG", t.message.toString())
            }

            override fun onResponse(call: Call<List<Shop>>, response: Response<List<Shop>>) {
                _shops.value = response.body()
            }
        })
    }

    override fun startInitialize() {
        allNearestShops()
    }
}
