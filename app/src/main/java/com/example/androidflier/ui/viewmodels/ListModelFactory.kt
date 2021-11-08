package com.example.androidflier.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidflier.ui.nearest.NearestListShopsViewModel

class ListModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //     TODO("Not yet implemented")
        //    return modelClass.getConstructor(Context::class.java).newInstance(context)
        val viewModel =
            when (modelClass) {
                NearestListShopsViewModel::class.java -> NearestListShopsViewModel() as T

                else -> throw IllegalStateException("is not view model")
            }

        return viewModel
    }
}