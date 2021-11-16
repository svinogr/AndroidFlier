package com.example.androidflier.ui.nearest

import android.app.Application
import android.provider.Settings
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

class NearestListShopsViewModel(context: Application) : BaseShopViewModel(context) {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops

    fun allNearestShops() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            shopRepo.getAllNearestShops().enqueue(object : Callback<List<Shop>> {
                override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                }

                override fun onResponse(call: Call<List<Shop>>, response: Response<List<Shop>>) {
                    _shops.postValue(response.body())
                }
            })
        }
    }

    override fun refreshData() {
        allNearestShops()
    }
}
