package uz.gita.saiga_driver.data.remote.response.auth

// Created by Jamshid Isoqov on 2/6/2023
data class BalanceResponse(
    val user: UserResponse,
    val balance: String
)
