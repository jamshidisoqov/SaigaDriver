package uz.gita.saiga_driver.data.remote.response

import com.google.gson.annotations.SerializedName

// Created by Jamshid Isoqov on 2/26/2023
data class StaticAddressResponse(
    val id: Long,
    val title: String,
    val district: String,
    @SerializedName("latitude")
    val lat: Double,
    @SerializedName("longitude")
    val lon: Double
)
