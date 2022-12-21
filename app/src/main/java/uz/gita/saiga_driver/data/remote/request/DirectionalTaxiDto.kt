package uz.gita.saiga_driver.data.remote.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uz.gita.saiga_driver.data.remote.response.DirectionsData
import java.sql.Timestamp

// Created by Jamshid Isoqov on 12/17/2022
@Parcelize
data class DirectionalTaxiDto(
    val directionsDto: DirectionsDto,
    val scheduleTime: Timestamp,
    val price: Double
) : Parcelable
