package uz.gita.saiga_driver.data.remote.response.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Created by Jamshid Isoqov on 1/31/2023
@Parcelize
data class AddressResponse(
    val id:Long,
    val lat: Double? = null,
    val lon: Double? = null,
    val title: String? = null
):Parcelable