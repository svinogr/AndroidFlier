package com.example.androidflier.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.androidflier.repo.ShopRepository

abstract class BaseShopModel: ViewModel() {
    open val shopRepo = ShopRepository.getInstance()

    abstract fun startInitialize()
}