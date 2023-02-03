package uz.gita.saiga_driver.data.remote.response.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uz.gita.saiga_driver.data.remote.request.auth.enums.Lang
import uz.gita.saiga_driver.data.remote.request.auth.enums.Role
import uz.gita.saiga_driver.data.remote.response.auth.enums.Status

// Created by Jamshid Isoqov on 1/26/2023
@Parcelize
data class UserResponse(
    val id: Long,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val status: Status,
    val role: Role = Role.USER,
    val lang: Lang = Lang.KAA
):Parcelable
