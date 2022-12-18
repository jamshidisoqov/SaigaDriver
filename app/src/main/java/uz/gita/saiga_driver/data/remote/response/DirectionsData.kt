package uz.gita.saiga_driver.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uz.gita.saiga_driver.data.remote.response.AddressData

// Created by Jamshid Isoqov on 12/15/2022
@Parcelize
data class DirectionsData(
    val id:Long,
    val addressFrom: AddressData,
    val addressTo: AddressData
):Parcelable
