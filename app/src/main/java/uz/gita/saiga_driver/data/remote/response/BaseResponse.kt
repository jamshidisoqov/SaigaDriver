package uz.gita.saiga_driver.data.remote.response

// Created by Jamshid Isoqov on 1/26/2023
data class BaseResponse<T>(
    val message: String? = null,
    val active: Boolean? = null,
    val body: T
)
