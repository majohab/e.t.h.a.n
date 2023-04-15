package com.example.ethan.usecases

import com.example.ethan.api.connectors.OpenStreetMapApi

class LunchBreakConsultant(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var resTimeID = "time_LBC"
    private var openStreetMapRestaurant = OpenStreetMapApi()
    override fun executeUseCase() {
        val restaurants = openStreetMapRestaurant.findNearestRestaurants(37.7749, -122.4194, 1000 , "italian")
        println(restaurants)
    }
}