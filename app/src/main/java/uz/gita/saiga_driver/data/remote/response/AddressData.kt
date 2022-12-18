package uz.gita.saiga_driver.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Created by Jamshid Isoqov on 12/15/2022
@Parcelize
data class AddressData(
    val id: Long,
    val title: String,
    val lat: Double? = null,
    val lon: Double? = null
):Parcelable


