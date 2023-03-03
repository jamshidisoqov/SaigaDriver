package uz.gita.saiga_driver.data.remote.request.direction

// Created by Jamshid Isoqov on 2/26/2023
data class StaticAddressRequest(
    val title: String,
    val district: String,
    val lat: Double,
    val lon: Double
)