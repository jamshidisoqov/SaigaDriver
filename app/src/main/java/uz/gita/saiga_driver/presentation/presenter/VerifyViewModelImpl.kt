package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.directions.VerifyScreenDirection
import uz.gita.saiga_driver.domain.repository.AuthRepository
import uz.gita.saiga_driver.presentation.ui.verify.VerifyViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class VerifyViewModelImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val mySharedPref: MySharedPref
) : VerifyViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val openPermissionChecker = MutableSharedFlow<Unit>()

    override fun resendCode() {
        viewModelScope.launch {
            if (hasConnection()) {
                authRepository.resendCode().collectLatest {result->
                    result.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                }
            }else{
                messageSharedFlow.emit("No internet connection")
            }
        }
    }

    override fun verifyCode(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (hasConnection()) {
                loadingSharedFlow.emit(true)
                authRepository.verifyCode(code).collectLatest { result ->
                    loadingSharedFlow.emit(false)
                    result.onSuccess {
                        authRepository.saveUser(it.body.userResponse, it.body.token)
                        mySharedPref.isVerify = true
                        openPermissionChecker.emit(Unit)
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                }
            }else{
                messageSharedFlow.emit("No internet connection")
            }
        }
    }
}