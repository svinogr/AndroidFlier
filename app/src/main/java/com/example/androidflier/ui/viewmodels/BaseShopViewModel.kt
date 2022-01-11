package com.example.androidflier.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Tab
import com.example.androidflier.repo.ShopRepository
import com.example.androidflier.repo.ShopRepositoryable
import com.example.androidflier.repo.TabReposable
import com.example.androidflier.repo.localdb.LocalDataStorageable
import com.example.androidflier.repo.retrofit.RetrofitInst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseShopViewModel(val context: Application): ViewModel() {
    var shopRepo: ShopRepositoryable
    var tabRepo: TabReposable
    var localDb: LocalDataStorageable
    val _message = MutableLiveData<String>()
    val message = _message
    private val _tabs = MutableLiveData<List<Tab>>()
    val tabs: LiveData<List<Tab>> = _tabs

    final val delayRefresh: Long = 700

    init {
        val flierApp = context as FlierApp
        shopRepo = flierApp.shopRepo
        localDb = flierApp.localDb
        tabRepo = flierApp.tabRepo
    }

    open fun allTabs(tab: Tab?) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            Log.d("allTabs", " allTabs start")

            tabRepo.getAllTab().enqueue(object : Callback<List<Tab>> {
                override fun onResponse(call: Call<List<Tab>>, response: Response<List<Tab>>) {
                    if (response.code() != 200) {
                        message.postValue(response.message())
                        _tabs.postValue(listOf())
                    } else {
                        val body = response.body()


                        if (tab != null) {
                            body?.forEach {
                                if (it.name == tab.name)
                                    it.selected = true
                                Log.d("forEach", " allTab $it")

                            }

                        }
                        _tabs.postValue(body!!)
                    }
                }

                override fun onFailure(call: Call<List<Tab>>, t: Throwable) {
                    message.postValue(t.message)
                    _tabs.postValue(listOf())
                }
            })

            Log.d("DashboardViewModel", " allTabs end")
        }
    }

}