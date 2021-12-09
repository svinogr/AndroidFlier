package com.example.androidflier.repo

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationRepoTEST private constructor(): LocationReposable {
    private lateinit var locationClient: FusedLocationProviderClient

    companion object {
        private lateinit var instance: LocationRepoTEST

        init {
            instance = LocationRepoTEST()
        }

        fun getInstance(context: Context): LocationRepoTEST {
            instance.locationClient = LocationServices.getFusedLocationProviderClient(context)
            return instance
        }
    }


    override fun getLocateClient(): FusedLocationProviderClient {
        return locationClient
    }
}