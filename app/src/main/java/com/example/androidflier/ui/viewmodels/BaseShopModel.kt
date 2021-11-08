package com.example.androidflier.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp
import com.example.androidflier.repo.ShopRepository
import com.example.androidflier.repo.retrofit.RetrofitInst

abstract class BaseShopModel: ViewModel() {
     val shopRepo = ShopRepository.getInstance()

    abstract fun startInitialize()
}