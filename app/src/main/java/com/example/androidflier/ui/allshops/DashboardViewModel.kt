package com.example.androidflier.ui.allshops

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.BaseShopViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel(context: Context) : BaseShopViewModel(context) {

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops
    private val _loading = MutableLiveData<Boolean>()

  /*  init {
        allShops()
    }*/

   private fun allShops() {
      GlobalScope.launch (Dispatchers.IO){
          delay(delayRefresh)

          shopRepo.getAllShops().enqueue(object : Callback<List<Shop>> {
              override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                  Log.d("TAG", t.message.toString())
                  Log.d("TAG", call.toString())
                  _shops.value = listOf()
              }

              override fun onResponse(call: Call<List<Shop>>, response: Response<List<Shop>>) {
                //  _shops.postValue(response.body())
                  Log.d("ref", "call onResponse")
                  _shops.value  = response.body()
              }
          })
      }
    }

    override fun refreshData() {
        allShops()
    }
}