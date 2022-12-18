package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.directions.VerifyScreenDirection
import uz.gita.saiga_driver.domain.repository.auth.AuthRepository
import uz.gita.saiga_driver.presentation.ui.verify.VerifyViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import javax.inject.Inject

@HiltViewModel
class VerifyViewModelImpl @Inject constructor(
    private val direction: VerifyScreenDirection,
    private val authRepository: AuthRepository
) : VerifyViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override fun resendCode() {

    }

    override fun verifyCode(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingSharedFlow.emit(true)
            authRepository.verifyCode(code).collectLatest { result ->
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
}