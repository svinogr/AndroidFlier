package com.example.androidflier.ui.shopdetail

import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.BaseShopModel

/*
class ShopViewModel1(val id: Long ): ViewModel() {
    private val shopRepo = ShopRepository.getInstance()
    private  var _shop = MutableLiveData<Shop>()
    val shop = _shop

    init {
       getShopById(id)
    }

    fun getShopById(id: Long) {
        _shop.value = shopRepo.getShopById(id)
    }
}
*/

class ShopViewModel(val id: Long) : BaseShopModel() {
    private var _shop = MutableLiveData<Shop>()
    val shop = _shop



    init {
        startInitialize()
    }

    override fun startInitialize() {
        _shop.value = shopRepo.getShopById(id)
    }
}
