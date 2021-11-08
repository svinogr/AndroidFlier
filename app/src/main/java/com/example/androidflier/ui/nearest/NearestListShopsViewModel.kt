package com.example.androidflier.ui.nearest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.BaseShopModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NearestListShopsViewModel() : BaseShopModel() {

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops
    //var shops = savedState.getLiveData<List<Shop>>("shop")

    init {
        startInitialize()
    }

    fun allNearestShopsTest() {
        val shopsList = shopRepo.getAllShop()
        //  shops.postValue(shopRepo.getAllTest())
        shopsList.enqueue(object : Callback<List<Shop>> {
            override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                Log.d("TAG", t.message.toString())
            }

            override fun onResponse(call: Call<List<Shop>>, response: Response<List<Shop>>) {
                _shops.value = response.body()
            }
        })
        //savedState.set("shop", shopsList)
    }

    override fun startInitialize() {
        allNearestShopsTest()
    }


}