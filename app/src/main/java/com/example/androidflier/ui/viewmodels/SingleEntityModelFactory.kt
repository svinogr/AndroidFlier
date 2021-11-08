package com.example.androidflier.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidflier.ui.shopdetail.ShopViewModel

class SingleEntityModelFactory(val id: Long) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel =
            when(modelClass) {
                ShopViewModel::class.java -> ShopViewModel(id) as T
                else -> throw IllegalStateException("is not view model")
            }

        return viewModel
    }
}