package uz.gita.saiga_driver.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

// Created by Jamshid Isoqov on 12/17/2022
@Parcelize
data class DirectionalTaxiData(
    val id: Long,
    val directionsData: DirectionsData,
    val scheduleTime: Timestamp,
    val price: Double
):Parcelable
