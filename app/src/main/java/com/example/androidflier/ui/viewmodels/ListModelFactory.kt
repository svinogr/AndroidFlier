package com.example.androidflier.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidflier.ui.allshops.DashboardViewModel
import com.example.androidflier.ui.nearest.NearestListShopsViewModel

class ListModelFactory(val context: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel =
            when (modelClass) {
                NearestListShopsViewModel::class.java -> NearestListShopsViewModel(context) as T
                DashboardViewModel::class.java -> DashboardViewModel(context) as T

                else -> throw IllegalStateException("is not view model")
            }

        return viewModel
    }
}