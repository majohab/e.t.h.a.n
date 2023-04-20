package com.example.ethan

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import org.json.JSONObject

object LocalLocation {

    var fusedLocationClient: FusedLocationProviderClient? = null

    fun initLocalLocation(activity: Activity){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    // Permission request PopUp cannot be started from here, so user needs to grant manually
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(): JSONObject{
        // Default coordinates of Lerchenstraße 1, if no location could be found
        val result = JSONObject("{\"lat\":\"48.782869\",\"lon\":\"9.167049\"}")

        if (fusedLocationClient == null){ return result}
        fusedLocationClient!!.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                // When location could be retrieved
                if (location != null){
                    result.put("lat", location.latitude.toString())
                    result.put("lon", location.longitude.toString())
                }

            }
            .addOnFailureListener {ex: Exception ->
                println(ex)
            }

        return result
    }
}