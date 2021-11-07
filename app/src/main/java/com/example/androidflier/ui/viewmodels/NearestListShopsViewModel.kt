package com.example.androidflier.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.ShopRepository

class NearestListShopsViewModel() : BaseShopModel() {

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops
    //var shops = savedState.getLiveData<List<Shop>>("shop")

    init {
      startInitialize()
    }

    fun allNearestShopsTest() {
        val shopsList = shopRepo.getAllTestNearestShop()
        //  shops.postValue(shopRepo.getAllTest())
        _shops.value = shopsList
        //savedState.set("shop", shopsList)
    }

    override fun startInitialize() {
        allNearestShopsTest()
    }


}