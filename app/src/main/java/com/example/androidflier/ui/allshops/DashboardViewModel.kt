package com.example.androidflier.ui.allshops

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Tab
import com.example.androidflier.ui.viewmodels.BaseShopViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel(context: Application) : BaseShopViewModel(context) {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops
    private val _tabs = MutableLiveData<List<Tab>>()
    val tab: LiveData<List<Tab>> = _tabs

    private fun allShops() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            shopRepo.getAllShops().enqueue(object : Callback<List<Shop>> {
                override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                    Log.d("DashboardViewModel", "fail: $t.toString()")
                    _message.postValue(t.message)
                    _shops.postValue(listOf())
                }

                override fun onResponse(call: Call<List<Shop>>, response: Response<List<Shop>>) {
                    Log.d("DashboardViewModel", "resp: $response.toString()")
                    if (response.code() != 200) {
                        message.postValue(response.message())
                        _shops.postValue(listOf())
                    } else {
                        _shops.postValue(response.body())
                    }
                }
            })
        }
    }

    private fun allTabs() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            Log.d("DashboardViewModel", " allTabs")

            val testString: List<Tab> = listOf(
                Tab(1,"строка"),
                Tab(2,"строка 2"),
                Tab(3,"строка 3"),
                Tab(4,"строка 4"),
                Tab(5,"строка5"),
                Tab(6,"строка 6"),
                Tab(7,"строка 7"),
                Tab(8,"строка 8")
            )
            _tabs.postValue(testString)
            Log.d("DashboardViewModel", " allTabs")
        }
    }

    override fun refreshData() {
        Log.d("DashboardViewModel", " refreshData")
        allShops()
        allTabs()
    }

    fun getTab() {
        allTabs()
    }
}