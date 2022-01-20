package com.example.androidflier.ui.nearest

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.androidflier.R
import com.example.androidflier.ui.BaseLiveDataFragment
import com.example.androidflier.ui.viewmodels.ListModelFactory

class NearestFragment : BaseLiveDataFragment(R.layout.fragment_nearest) {


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

    override fun isNeedPermissions(): Boolean = true

}