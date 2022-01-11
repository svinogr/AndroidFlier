package com.example.androidflier.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationLiveData(context: Context) : LiveData<Location>() {
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private fun setLocation(location: Location) {
        value = location
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            // super.onLocationResult(p0)

            for (location in p0.locations) {
                setLocation(location)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocation(it)
                }
            }

        startLocationUpdate()
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}