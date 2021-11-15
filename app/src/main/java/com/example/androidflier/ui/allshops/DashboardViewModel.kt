package com.example.androidflier.ui.allshops

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.BaseShopViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel(context: Application) : BaseShopViewModel(context) {

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        startInitialize()
    }

    fun allShops() {
        _loading.value = true
        shopRepo.getAllShops().enqueue(object : Callback<List<Shop>> {
            override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<Shop>>, response: Response<List<Shop>>) {
                _shops.value = response.body()
                _loading.value = false
            }
        })
    }

    override fun startInitialize() {
        allShops()

    }
}