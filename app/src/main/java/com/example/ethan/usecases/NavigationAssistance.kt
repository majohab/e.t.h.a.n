package com.example.ethan.usecases

import com.example.ethan.BuildConfig
import com.example.ethan.api.connectors.RouteConnector
import okhttp3.FormBody
import okhttp3.Headers

class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {

    override fun run() {
        val movmentTypes = listOf("driving-car", "cycling-regular", "foot-walking")
        val coordinates = listOf(listOf(8.681495,49.41461), listOf(8.681495,49.41461))

        val requestBody = FormBody.Builder()
            .add("coordinates", coordinates.toString())
            .build()

        val requestHeaders = Headers.Builder()
            .add("Authorization", BuildConfig.API_KEY_Routes)
            .build()

        movmentTypes.forEach {
            val url = "https://api.openrouteservice.org/v2/directions/" + it + "/json"

            val route = RouteConnector(url, requestBody, requestHeaders)

            val response = route.post()

            println(response)

        }

        println("NavigationAssistance Thread has been started!")

    }
}