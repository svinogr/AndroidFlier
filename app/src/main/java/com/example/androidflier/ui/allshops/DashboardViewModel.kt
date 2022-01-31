package com.example.androidflier.ui.allshops

import android.app.Application
import android.util.Log
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Tab
import com.example.androidflier.ui.viewmodels.BaseShopsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardViewModel(context: Application) : BaseShopsViewModel(context) {

    override fun getData(tab: Tab?, searchText: String) {
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

                            if (_shops.value.isNullOrEmpty()) {
                                _shops.value = mutableListOf()
                            }

                            val m = mutableListOf<Shop>()
                            m.addAll(response.body()!!)

                            if (!m.isEmpty()) {
                                _shops.value = (_shops.value)?.plus(m)
                            }
                        }
                    }
                })
        }
    }

    override fun clearData() {
        Log.d("DashboardViewModel", "clear")
        _shops.value = emptyList()
    }

    override fun refreshDataSearch(selectedTab: Tab?, searchText: String) {
        getData(selectedTab, searchText)
        allTabs(selectedTab)
        Log.d("DashboardViewModel", "$searchText  $selectedTab")
    }

    override fun loadMore(tab: Tab?, searchText: String) {
        Log.d("DashboardViewModel", "Load more")

        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)
            val from = (_shops.value!!.size).toString()
            shopRepo.getAllShopsWithSearching(tab, searchText, from)
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
                            //   _shops.postValue(response.body())
                            val m = mutableListOf<Shop>()
                            m.addAll(response.body()!!)
                            if (!m.isEmpty()) {
                                _shops.value = (_shops.value)?.plus(m)
                            }
                        }
                    }
                })
        }
    }
}