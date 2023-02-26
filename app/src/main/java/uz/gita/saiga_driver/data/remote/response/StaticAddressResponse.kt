package uz.gita.saiga_driver.data.remote.response

// Created by Jamshid Isoqov on 2/26/2023
data class StaticAddressResponse(
    val id: Long,
    val title: String,
    val district: String,
    val lat: Double,
    val lon: Double
)
