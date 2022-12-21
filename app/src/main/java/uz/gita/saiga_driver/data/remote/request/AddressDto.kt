package uz.gita.saiga_driver.data.remote.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Created by Jamshid Isoqov on 12/15/2022
@Parcelize
data class AddressDto(
    val title: String,
    val lat: Double? = null,
    val lon: Double? = null
):Parcelable


