package uz.gita.saiga_driver.data.remote.request.auth

import uz.gita.saiga_driver.data.remote.request.auth.enums.Role

// Created by Jamshid Isoqov on 12/15/2022
data class UserRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val role: Role = Role.DRIVER,
)
