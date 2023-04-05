package com.example.ethan.usecases

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

class NavigationAssistance (onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {

    override fun run() {
        println("GoodMorningDialogue Thread has been started!")
    }



}