package com.example.androidflier.ui.nearest

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
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
    fun allNearestShops() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            locationReposable.getLocateClient().lastLocation.addOnCompleteListener { location ->

                val loc = location.result

                if (loc != null) {
                    Log.d("loc", loc.latitude.toString())

                    shopRepo.getAllNearestShops(loc).enqueue(object : Callback<List<Shop>> {
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

    override fun refreshData() {
        allNearestShops()
    }
}
