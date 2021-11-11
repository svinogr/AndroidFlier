package com.example.androidflier.ui.allshops

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.BaseShopModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel(context: Application) : BaseShopModel(context) {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops

    init {
        startInitialize()
    }

    fun allShops() {
        shopRepo.getAllShops().enqueue(object : Callback<List<Shop>> {
            override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                Log.d("TAG", t.message.toString())
            }

            override fun onResponse(call: Call<List<Shop>>, response: Response<List<Shop>>) {
                _shops.value = response.body()
            }
        })
    }

    override fun startInitialize() {
        allShops()
    }
}