package com.example.androidflier.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.androidflier.FlierApp
import com.example.androidflier.repo.retrofit.RetrofitInst

abstract class BaseShopModel: ViewModel() {
     val shopRepo = RetrofitInst.getInstance().shopApi

    abstract fun startInitialize()
}