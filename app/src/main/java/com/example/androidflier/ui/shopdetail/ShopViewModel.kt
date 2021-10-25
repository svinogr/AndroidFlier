package com.example.androidflier.ui.shopdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.ShopRepository

class ShopViewModel(val id: Long ): ViewModel() {
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