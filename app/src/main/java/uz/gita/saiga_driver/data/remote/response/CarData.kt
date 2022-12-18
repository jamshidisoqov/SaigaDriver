package uz.gita.saiga_driver.data.remote.response

// Created by Jamshid Isoqov on 12/15/2022
data class CarData(
    val id: Long,
    val model: CarModelData,
    val number:String,
    val color:String
)
