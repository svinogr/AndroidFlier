package com.example.androidflier.ui.nearest

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.model.Tab
import com.example.androidflier.repo.LocationRepo
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
    private val locationReposable = LocationRepo.getInstance(context)

    @SuppressLint("MissingPermission")
    fun allNearestShops(tab: Tab?, searchText: String) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            locationReposable.getLocateClient().lastLocation.addOnCompleteListener { location ->

                val loc = location.result
              //TODO for testing GPS

                if (loc != null) {
                    Log.d("loc", loc.latitude.toString())

                    shopRepo.getAllNearestShopsWithSearching(loc, tab, searchText)
                        .enqueue(object : Callback<List<Shop>> {
                            override fun onFailure(call: Call<List<Shop>>, t: Throwable) {
                                Log.d("NearestListShopsViewModel", "fail: $t.toString()")
                                _message.postValue(t.message)
                                _shops.postValue(listOf())
                            }

                            override fun onResponse(
                                call: Call<List<Shop>>,
                                response: Response<List<Shop>>
                            ) {
                                Log.d("NearestListShopsViewModel", "resp: $response.toString()")
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
        }
    }

    /*  open fun allTabs(tab: Tab?) {
          GlobalScope.launch(Dispatchers.IO) {
              delay(delayRefresh)

              Log.d("DashboardViewModel", " allTabs")

              val testString: List<Tab> = listOf(
                  Tab(1, "строка"),
                  Tab(2, "строка 2"),
                  Tab(3, "строка 3"),
                  Tab(4, "строка 4"),
                  Tab(5, "строка5"),
                  Tab(6, "строка 6"),
                  Tab(7, "строка 7"),
                  Tab(8, "строка 8")
              )

              // проверяем ессли выбрана таб то отоборазим как выбраную
              if (tab != null) {
                  testString.forEach {
                      if (it.name == tab.name)
                          it.selected = true
                      Log.d("forEach", " allTab $it")

                  }
              }

              _tabs.postValue(testString)
              Log.d("DashboardViewModel", " allTabs")
          }
      }*/

    fun refreshDataSearch(selectedTab: Tab?, searchText: String) {
        allNearestShops(selectedTab, searchText)
        allTabs(selectedTab)
        Log.d("NearestListShopsViewModel", "$searchText  $selectedTab")
    }
}
