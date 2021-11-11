package com.example.androidflier.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp
import com.example.androidflier.repo.ShopRepository
import com.example.androidflier.repo.ShopRepositoryable
import com.example.androidflier.repo.retrofit.RetrofitInst

abstract class BaseShopModel(open val context: Application): ViewModel() {
    var shopRepo: ShopRepositoryable

    init {
        val flierApp = context as FlierApp
        shopRepo = flierApp.shopRepo
    }

    abstract fun startInitialize()
}