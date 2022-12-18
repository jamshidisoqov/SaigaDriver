package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.directions.LoginScreenDirection
import uz.gita.saiga_driver.domain.repository.auth.AuthRepository
import uz.gita.saiga_driver.presentation.ui.login.LoginViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import javax.inject.Inject

class LoginViewModelImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val direction: LoginScreenDirection,
    private val mySharedPref: MySharedPref
) : LoginViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override fun navigateToRegister() {
        viewModelScope.launch {
            direction.navigateToRegisterScreen()
        }
    }

    override fun login(phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingSharedFlow.emit(true)
            authRepository.login(phone)
                .collectLatest { result ->
                    loadingSharedFlow.emit(false)
                    result.onSuccess {
                        //TODO navigate and save shared prefs
                        direction.navigateToVerifyScreen(phone)
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                }
        }
    }

}