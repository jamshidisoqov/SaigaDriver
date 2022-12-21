package uz.gita.saiga_driver.data.remote.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uz.gita.saiga_driver.data.remote.response.AddressData

// Created by Jamshid Isoqov on 12/15/2022
@Parcelize
data class DirectionsDto(
    val addressFrom: AddressDto,
    val addressTo: AddressDto
):Parcelable
