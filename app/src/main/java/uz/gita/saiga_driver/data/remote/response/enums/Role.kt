package uz.gita.saiga_driver.data.remote.response.enums

// Created by Jamshid Isoqov an 11/22/2022
enum class RoleEnum {
    USER,
    DRIVER
}

data class Role(
    val id: Int,
    val role: RoleEnum
)