package com.example.androidflier.ui.shopdetail

import androidx.lifecycle.MutableLiveData
import com.example.androidflier.model.Shop
import com.example.androidflier.ui.viewmodels.BaseShopModel

class ShopViewModel(val id: Long) : BaseShopModel() {
    private var _shop = MutableLiveData<Shop>()
    val shop = _shop

    init {
        startInitialize()
    }

    override fun startInitialize() {
      //  _shop.value = shopRepo.getShopById(id)
    }
}
