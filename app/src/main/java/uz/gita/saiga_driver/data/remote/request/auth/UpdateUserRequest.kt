package uz.gita.saiga_driver.data.remote.request.auth

import uz.gita.saiga_driver.data.remote.request.auth.enums.Lang

// Created by Jamshid Isoqov on 1/26/2023
data class UpdateUserRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber : String,
    val lang: Lang = Lang.KAA
)
