package com.example.androidflier.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp

abstract class BaseShopModel: ViewModel() {
    open val shopRepo = FlierApp.shopRepository

    abstract fun startInitialize()
}