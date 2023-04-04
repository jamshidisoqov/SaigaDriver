package uz.gita.saiga_driver.domain.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.response.DriverBalanceResponse
import uz.gita.saiga_driver.data.remote.response.auth.*
import uz.gita.saiga_driver.domain.enums.StartScreen
import uz.gita.saiga_driver.utils.ResultData

// Created by Jamshid Isoqov on 12/14/2022
interface AuthRepository {

    suspend fun getStartScreen(): StartScreen

    fun login(phoneNumber: String): Flow<ResultData<AuthResponse>>

    fun register(
        phoneNumber: String,
        firstName: String,
        lastName: String
    ): Flow<ResultData<CabinetBaseResponse>>

    fun verifyCode(code: String): Flow<ResultData<String>>

    fun resendCode()

    fun sendSms(phone: String):Flow<ResultData<String>>

    fun updateUser(firstName: String, lastName: String): Flow<ResultData<AuthResponse>>

    suspend fun updateUser():Any

    fun getUserData():Flow<ResultData<AuthResponse>>

    fun topUpBalance(amount:Long):Flow<ResultData<BalanceResponse>>

    fun getUserBalance(): Flow<ResultData<DriverBalanceResponse>>

}