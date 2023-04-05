package com.example.ethan.api

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import com.google.android.gms.location.LocationServices

class Location : Activity() {



    @SuppressLint("MissingPermission")
    fun getLocation(){
        // Code required for requesting location permissions omitted for brevity.

        //TO-DO was ist activity
        val client = LocationServices.getFusedLocationProviderClient(this)

        // Get the last known location. In some rare situations, this can be null
        client.lastLocation.addOnSuccessListener { location : Location? ->
            location?.let {
                // Logic to handle location object.
            }
        }

    }
}