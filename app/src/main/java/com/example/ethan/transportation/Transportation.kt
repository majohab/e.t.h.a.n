package com.example.ethan.transportation

var transportTranslations = mapOf(
    "foot-walking" to "foot",
    "cycling-regular" to "cycling",
    "driving-car" to "car",
    "wheelchair" to "wheelchair"
)

fun getAllTransportationKeys() : List<String> {
    return transportTranslations.map { it.key }
}