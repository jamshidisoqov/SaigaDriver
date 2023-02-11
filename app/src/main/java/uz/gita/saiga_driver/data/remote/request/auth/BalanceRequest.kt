package uz.gita.saiga_driver.data.remote.request.auth

// Created by Jamshid Isoqov on 2/6/2023
data class BalanceRequest(
    val amount: String,
    val userID: Long
)
