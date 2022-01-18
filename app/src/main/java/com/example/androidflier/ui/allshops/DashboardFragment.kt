package com.example.androidflier.ui.allshops

import androidx.lifecycle.ViewModelProvider
import com.example.androidflier.R
import com.example.androidflier.ui.BaseLiveDataFragment
import com.example.androidflier.ui.viewmodels.ListModelFactory


class DashboardFragment : BaseLiveDataFragment(R.layout.fragment_dashboard) {

    companion object {
        const val TAG = "DashboardFragment"
    }

    override fun setViewModel() {
        shopsViewModel = ViewModelProvider(
            requireActivity(),
            ListModelFactory(requireActivity().application)
        ).get(TAG, DashboardViewModel::class.java)
    }

}
