package uz.gita.saiga_driver.data.remote.response

import uz.gita.saiga_driver.data.remote.response.enums.CarType

// Created by Jamshid Isoqov on 12/15/2022
data class CarModelData(
    val id:Long,
    val type: CarType,
    val name:String
)
