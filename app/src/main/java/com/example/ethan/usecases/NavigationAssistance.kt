package com.example.ethan.usecases

import com.example.ethan.api.connectors.OpenRouteConnector
import com.example.ethan.api.connectors.OpenWeatherApiConnector

class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var resTimeID = "time_NA"

    private var navigationConnector = OpenRouteConnector()
    private var weatherApiConnector = OpenWeatherApiConnector()

    override fun executeUseCase() {
        val duration = navigationConnector.getRouteDuration("48.783821, 9.215519", "Milaneo", "foot-walking")
        println(duration)
        val weatherJSON = weatherApiConnector.getCurrentWeather(48.783821, 9.215519)
        println(weatherJSON)
    }
}