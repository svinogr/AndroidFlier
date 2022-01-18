package com.example.androidflier.ui.nearest

import androidx.lifecycle.ViewModelProvider
import com.example.androidflier.R
import com.example.androidflier.ui.BaseLiveDataFragment
import com.example.androidflier.ui.viewmodels.ListModelFactory

class NearestFragment: BaseLiveDataFragment(R.layout.fragment_nearest) {
    companion object {
        const val TAG = "NearestFragment"
    }

    override fun setViewModel() {
        shopsViewModel = ViewModelProvider(
            requireActivity(),
            ListModelFactory(requireActivity().application)
        ).get(
            TAG,
            NearestListShopsViewModel::class.java
        )
    }
}