package uz.gita.saiga_driver.data.remote.response

import uz.gita.saiga_driver.data.remote.response.enums.Status

// Created by Jamshid Isoqov on 12/15/2022
data class CabinetData(
    val id: Long,
    val user: UserData,
    val region: AddressData? = null,
    val amount: Double = 0.0,
    val status: Status = Status.ACTIVE,
    val directions: Set<DirectionsData>? = null,
    val car: CarData? = null
)
