package uz.gita.saiga_driver.data.remote.response.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Created by Jamshid Isoqov on 1/31/2023
@Parcelize
data class DirectionResponse(
    val id:Long,
    val addressFrom:AddressResponse,
    val addressTo:AddressResponse?=null,
    val isFavourite:Boolean = false,
):Parcelable
