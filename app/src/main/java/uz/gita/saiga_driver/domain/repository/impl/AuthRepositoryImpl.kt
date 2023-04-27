package uz.gita.saiga_driver.domain.repository.impl

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.data.remote.api.AuthApi
import uz.gita.saiga_driver.data.remote.request.auth.BalanceRequest
import uz.gita.saiga_driver.data.remote.request.auth.LoginRequest
import uz.gita.saiga_driver.data.remote.request.auth.SmsRequest
import uz.gita.saiga_driver.data.remote.request.auth.UpdateUserRequest
import uz.gita.saiga_driver.data.remote.request.auth.UserRequest
import uz.gita.saiga_driver.data.remote.request.auth.enums.Lang
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.response.DriverBalanceResponse
import uz.gita.saiga_driver.data.remote.response.auth.AuthResponse
import uz.gita.saiga_driver.data.remote.response.auth.BalanceResponse
import uz.gita.saiga_driver.data.remote.response.auth.UserBaseResponse
import uz.gita.saiga_driver.data.remote.response.auth.UserResponse
import uz.gita.saiga_driver.domain.enums.StartScreen
import uz.gita.saiga_driver.domain.repository.AuthRepository
import uz.gita.saiga_driver.utils.ResultData
import uz.gita.saiga_driver.utils.extensions.func
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val mySharedPref: MySharedPref,
    private val authApi: AuthApi,
    private val gson: Gson
) : AuthRepository {

    override suspend fun getStartScreen(): StartScreen {
        return if (mySharedPref.isVerify) StartScreen.MAIN
        else StartScreen.LOGIN
    }

    override fun login(phoneNumber: String): Flow<ResultData<Any>> =
        flow<ResultData<Any>> {
            val phone = phoneNumber.replace(" ", "")
            mySharedPref.phoneNumber = phone
            authApi.login(LoginRequest(phone)).func(gson).onSuccess {
                emit(ResultData.Success(it.body))
            }.onMessage {
                emit(ResultData.Message(it))
            }.onError {
                emit(ResultData.Error(it))
            }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    override fun register(
        phoneNumber: String, firstName: String, lastName: String
    ): Flow<ResultData<Any>> = flow<ResultData<Any>> {
        val phone = phoneNumber.replace(" ", "")
        mySharedPref.phoneNumber = phone
        authApi.registerUser(UserRequest(firstName, lastName, phone))
            .func(gson).onSuccess {
                emit(ResultData.Success(it.body))
            }.onMessage {
                emit(ResultData.Message(it))
            }.onError {
                emit(ResultData.Error(it))
            }
    }.catch { error ->
        emit(ResultData.Error(error))
    }.flowOn(Dispatchers.IO)

    override fun verifyCode(code: String): Flow<ResultData<BaseResponse<UserBaseResponse>>> =
        flow {
            emit(
                authApi.verifySms(
                    smsRequest = SmsRequest(
                        smsCode = code,
                        phone = mySharedPref.phoneNumber
                    )
                ).func(gson)
            )
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)


    override suspend fun resendCode() = login(mySharedPref.phoneNumber)


    override fun updateUser(firstName: String, lastName: String): Flow<ResultData<AuthResponse>> =
        flow<ResultData<AuthResponse>> {
            authApi.updateUser(
                mySharedPref.userId,
                UpdateUserRequest(
                    firstName,
                    lastName,
                    mySharedPref.phoneNumber,
                    Lang.values()[mySharedPref.language]
                )
            )
                .func(gson)
                .onSuccess {
                    emit(ResultData.Success(it.body))
                }.onMessage {
                    emit(ResultData.Message(it))
                }.onError {
                    emit(ResultData.Error(it))
                }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    override fun getUserData(): Flow<ResultData<AuthResponse>> = flow {}

    override fun topUpBalance(amount: Long): Flow<ResultData<BalanceResponse>> =
        flow<ResultData<BalanceResponse>> {
            authApi.topUpBalance(BalanceRequest(amount.toString(), mySharedPref.userId)).func(gson)
                .onSuccess {
                    emit(ResultData.Success(it.body.data))
                }.onMessage {
                    emit(ResultData.Message(it))
                }.onError {
                    emit(ResultData.Error(it))
                }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    override fun getUserBalance(): Flow<ResultData<DriverBalanceResponse>> =
        flow<ResultData<DriverBalanceResponse>> {
            authApi.getUserBalance().func(gson)
                .onSuccess {
                    emit(ResultData.Success(it.body.data))
                }.onMessage {
                    emit(ResultData.Message(it))
                }.onError {
                    emit(ResultData.Error(it))
                }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)


    override suspend fun updateUser(): Any = authApi.updateUser(
        mySharedPref.userId,
        UpdateUserRequest(
            mySharedPref.firstName,
            mySharedPref.lastName,
            mySharedPref.phoneNumber,
            Lang.values()[mySharedPref.language]
        )
    )

    override suspend fun saveUser(userResponse: UserResponse, token: String) {
        mySharedPref.userId = userResponse.id
        mySharedPref.firstName = userResponse.firstName
        mySharedPref.lastName = userResponse.lastName
        mySharedPref.phoneNumber = userResponse.phoneNumber
        mySharedPref.language = userResponse.lang.ordinal
        mySharedPref.token = token
        mySharedPref.userId = userResponse.id
    }
}