package com.example.androidflier.repo

import com.google.android.gms.location.FusedLocationProviderClient

interface LocationReposable {
    fun getLocateClient(): FusedLocationProviderClient
}