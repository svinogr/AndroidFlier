package com.example.androidflier.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidflier.model.Shop
import com.example.androidflier.repo.ShopRepository

class NearestListShopsViewModel() : ViewModel() {

    private var shopRepo: ShopRepository = ShopRepository.getInstance()
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops
    //var shops = savedState.getLiveData<List<Shop>>("shop")

    init {
        allNearestShopsTest()
    }

    fun allNearestShopsTest() {
        val shopsList = shopRepo.getAllTestNearestShop()
        //  shops.postValue(shopRepo.getAllTest())
        _shops.value = shopsList
        //savedState.set("shop", shopsList)
    }


}