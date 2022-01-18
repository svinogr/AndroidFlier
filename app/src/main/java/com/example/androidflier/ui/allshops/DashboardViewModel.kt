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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardViewModel(context: Application) : BaseShopViewModel(context) {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops


    private fun allShops(tab: Tab?, searchText: String) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            shopRepo.getAllShopsWithSearching(tab, searchText)
                .enqueue(object : Callback<List<Shop>> {
                    override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                        Log.d("DashboardViewModel", "fail: $t.toString()")
                        _message.postValue(t.message)
                        _shops.postValue(mutableListOf())
                    }

                    override fun onResponse(
                        call: Call<List<Shop>>,
                        response: Response<List<Shop>>
                    ) {
                        Log.d("DashboardViewModel", "resp: $response.toString()")
                        if (response.code() != 200) {
                            message.postValue(response.message())
                            _shops.postValue(mutableListOf())
                        } else {
                            /* _shops.postValue(response.body())*/
                            val m = mutableListOf<Shop>()
                            m.addAll(response.body()!!)
                            _shops.value = m
                        }
                    }
                })
        }
    }


    fun refreshDataSearch(selectedTab: Tab?, searchText: String) {
        allShops(selectedTab, searchText)
        allTabs(selectedTab)
        Log.d("DashboardViewModel", "$searchText  $selectedTab")
    }

    fun loadMore(tab: Tab?, searchText: String) {
        Log.d("DashboardViewModel", "Load more")
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            shopRepo.getAllShopsWithSearching(tab, searchText, (_shops.value!!.size).toString())
                .enqueue(object : Callback<List<Shop>> {
                    override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                        Log.d("DashboardViewModel", "fail: $t.toString()")
                        _message.postValue(t.message)
                        _shops.postValue(mutableListOf())
                    }

                    override fun onResponse(
                        call: Call<List<Shop>>,
                        response: Response<List<Shop>>
                    ) {
                        Log.d("DashboardViewModel", "resp: $response.toString()")
                        if (response.code() != 200) {
                            message.postValue(response.message())
                            _shops.postValue(mutableListOf())
                        } else {
                            /* _shops.postValue(response.body())*/
                            val m = mutableListOf<Shop>()
                            m.addAll(response.body()!!)
                            _shops.value = (_shops.value)?.plus(m)
                        }
                    }
                })
        }

    }
}