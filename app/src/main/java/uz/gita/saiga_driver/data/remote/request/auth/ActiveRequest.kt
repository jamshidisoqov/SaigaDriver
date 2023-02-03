package uz.gita.saiga_driver.data.remote.request.auth

import uz.gita.saiga_driver.data.remote.response.auth.enums.Status

// Created by Jamshid Isoqov on 1/26/2023
data class ActiveRequest(
    val status: Status
)
