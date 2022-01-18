package com.example.androidflier.ui.shopdetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.ShopRepositoryable
import com.example.androidflier.repo.localdb.LocalDataStorageable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopViewModel(private val id: Long, context: Application): ViewModel()  {
    private var shopRepo: ShopRepositoryable
    private var _shop = MutableLiveData<Shop>()
    var localDb: LocalDataStorageable
    val _message = MutableLiveData<String>()
    val message = _message
    final val delayRefresh: Long = 700

    val shop: LiveData<Shop> = _shop

    init {
        val flierApp = context as FlierApp
        shopRepo = flierApp.shopRepo
        localDb = flierApp.localDb
    }

    fun getShop() {
        Log.d("ShopViewModel", "initialize")
        GlobalScope.launch(Dispatchers.IO) {
            delay(delayRefresh)

            shopRepo.getShopWithStocks(id).enqueue(object : Callback<Shop> {
                override fun onFailure(call: Call<Shop>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                    Log.d("DashboardViewModel", "fail: $t.toString()")
                    _message.postValue(t.message)
                    _shop.postValue(getNullShop())
                }

                override fun onResponse(call: Call<Shop>, response: Response<Shop>) {

                    Log.d("DashboardViewModel", "resp: $response.toString()")
                    if (response.code() != 200) {
                        message.postValue(response.message())
                        _shop.postValue(getNullShop())
                    } else {
                        val shop = response.body()

                        if (shop != null) {
                            val shopIs = localDb.getShopById(id)

                            if (shopIs != null) {
                                shop.favoriteStatus = shopIs.favoriteStatus
                            }

                            _shop.postValue(shop!!)
                        }
                    }
                }

            })
        }
    }

    fun changeFavoriteStatus() {
        GlobalScope.launch(Dispatchers.IO) {
            val saveOrUpdateShop = _shop.value!!
           // val deleteRow = localDb.delete(deleteShop)

            val byId = localDb.getShopById(saveOrUpdateShop.id)

            if (byId != null) {
                saveOrUpdateShop.favoriteStatus = !byId.favoriteStatus
              localDb.update(saveOrUpdateShop)
            } else {
                saveOrUpdateShop.favoriteStatus = true
                localDb.save(saveOrUpdateShop)
            }

            _shop.postValue(saveOrUpdateShop)
        }
    }

    fun refreshData() {
        getShop()
    }

    private fun getNullShop(): Shop {
        return Shop(
            0,
            created = "",
            updated = "",
            status = "",
            userId = 0,
            coordLng = 0.0,
            coordLat = 0.0,
            title = "",
            address = "",
            description = "",
            url = "",
            img = "",
            favoriteStatus = false,
            stocks = mutableListOf(),
            phone = "",
            countStock = 0
        )
    }
}
