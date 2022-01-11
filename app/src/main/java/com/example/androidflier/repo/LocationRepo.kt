package com.example.androidflier.repo

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationRepo private constructor(): LocationReposable{
    private lateinit var locationClient: FusedLocationProviderClient

    companion object {
        private lateinit var instance: LocationRepo

        init {
            instance = LocationRepo()
        }

        fun getInstance(context: Context): LocationRepo {
           instance.locationClient = LocationServices.getFusedLocationProviderClient(context)
            return instance
        }
    }


    override fun getLocateClient(): FusedLocationProviderClient {
        return locationClient
    }
}