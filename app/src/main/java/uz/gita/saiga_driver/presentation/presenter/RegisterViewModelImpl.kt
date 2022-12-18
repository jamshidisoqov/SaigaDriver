package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.directions.RegisterScreenDirection
import uz.gita.saiga_driver.domain.repository.auth.AuthRepository
import uz.gita.saiga_driver.presentation.ui.register.RegisterViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val direction: RegisterScreenDirection,
    private val mySharedPref: MySharedPref
) : RegisterViewModel, ViewModel() {


    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()


    override fun register(phone: String, firstName: String, lastName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingSharedFlow.emit(true)
            authRepository.register(phoneNumber = phone, firstName, lastName)
                .collectLatest { result ->
                    loadingSharedFlow.emit(false)
                    result.onSuccess {
                        //TODO navigate and save shared prefs
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                }
        }
    }

    override fun navigateToLogin() {
        viewModelScope.launch {
            direction.navigateToLogin()
        }
    }
}