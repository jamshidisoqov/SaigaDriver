package uz.gita.saiga_driver.domain.repository.auth

import kotlinx.coroutines.flow.Flow
import uz.gita.saiga_driver.data.remote.response.UserData
import uz.gita.saiga_driver.domain.enums.StartScreen
import uz.gita.saiga_driver.utils.ResultData

// Created by Jamshid Isoqov on 12/18/2022
interface AuthRepository {

    suspend fun getStartScreen(): StartScreen

    fun login(phoneNumber: String): Flow<ResultData<UserData>>

    fun register(
        phoneNumber: String,
        firstName: String,
        lastName: String
    ): Flow<ResultData<UserData>>

    fun verifyCode(code: String): Flow<ResultData<String>>

    fun resendCode()

    fun updateUser(firstName: String, lastName: String): Flow<ResultData<UserData>>

}