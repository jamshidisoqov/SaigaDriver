package uz.gita.saiga_driver.domain.repository.impl

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import uz.gita.saiga_driver.MainActivity
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.data.remote.api.AuthApi
import uz.gita.saiga_driver.data.remote.request.auth.BalanceRequest
import uz.gita.saiga_driver.data.remote.request.auth.LoginRequest
import uz.gita.saiga_driver.data.remote.request.auth.UpdateUserRequest
import uz.gita.saiga_driver.data.remote.request.auth.UserRequest
import uz.gita.saiga_driver.data.remote.response.DriverBalanceResponse
import uz.gita.saiga_driver.data.remote.response.auth.AuthResponse
import uz.gita.saiga_driver.data.remote.response.auth.BalanceResponse
import uz.gita.saiga_driver.domain.enums.StartScreen
import uz.gita.saiga_driver.domain.repository.AuthRepository
import uz.gita.saiga_driver.utils.ResultData
import uz.gita.saiga_driver.utils.extensions.func
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val mySharedPref: MySharedPref,
    private val firebaseAuth: FirebaseAuth,
    private val authApi: AuthApi,
    private val gson: Gson
) : AuthRepository {

    private lateinit var mVerificationId: String

    private lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken

    override suspend fun getStartScreen(): StartScreen {
        return if (mySharedPref.isVerify) StartScreen.MAIN
        else StartScreen.LOGIN
    }

    override fun login(phoneNumber: String): Flow<ResultData<AuthResponse>> =
        flow<ResultData<AuthResponse>> {
            authApi.login(LoginRequest(phoneNumber.replace(" ", ""))).func(gson).onSuccess {
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
    ): Flow<ResultData<AuthResponse>> = flow<ResultData<AuthResponse>> {
        authApi.registerUser(UserRequest(firstName, lastName, phoneNumber.replace(" ", "")))
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

    override fun verifyCode(code: String): Flow<ResultData<String>> =
        callbackFlow<ResultData<String>> {
            val credential = PhoneAuthProvider.getCredential(mVerificationId, code)
            signInWithPhoneAuthCredential(credential, onSuccess = {
                trySend(ResultData.Success("Success"))
            }) {
                trySend(ResultData.Error(it))
            }
            awaitClose()
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)


    override fun resendCode() {
        sendSms(mySharedPref.phoneNumber)
    }

    override fun sendSms(phone: String): Flow<ResultData<String>> = callbackFlow {
        val options =
            PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(phone)
                .setTimeout(0L, TimeUnit.SECONDS)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        trySend(ResultData.Success("Completed"))
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        trySend(ResultData.Error(p0))
                    }

                    override fun onCodeSent(
                        p0: String, p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(p0, p1)
                        mVerificationId = p0
                        mResendToken = p1
                        trySend(ResultData.Success("Completed"))
                    }
                }).setActivity(MainActivity.activity).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose()
    }.catch { error ->
        emit(ResultData.Error(error))
    }.flowOn(Dispatchers.IO)

    override fun updateUser(firstName: String, lastName: String): Flow<ResultData<AuthResponse>> =
        flow<ResultData<AuthResponse>> {
            authApi.updateUser(
                mySharedPref.userId,
                UpdateUserRequest(firstName, lastName, mySharedPref.phoneNumber)
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

    override fun getUserData(): Flow<ResultData<AuthResponse>> = flow {

    }

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
                    emit(ResultData.Success(it.body))
                }.onMessage {
                    emit(ResultData.Message(it))
                }.onError {
                    emit(ResultData.Error(it))
                }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential, onSuccess: () -> Unit, onFailure: (Exception) -> Unit
    ) {
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
            onSuccess.invoke()
        }.addOnFailureListener {
            onFailure.invoke(it)
        }.addOnCompleteListener {
            onSuccess.invoke()
        }
    }
}