package uz.gita.saiga_driver.data.remote.response

import uz.gita.saiga_driver.domain.entity.TripWithDate
import java.sql.Timestamp

// Created by Jamshid Isoqov on 12/15/2022
data class OrderData(
    val id: Long,
    val direction: DirectionsData,
    val price: Double,
    val driver: CabinetData,
    val orderedUser: UserData,
    val createdDate: Timestamp,
    val updatedDate: Timestamp
) : TripWithDate
