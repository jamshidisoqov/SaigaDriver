package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.domain.repository.auth.AuthRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.profile.detail.ProfileDetailViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModelImpl @Inject constructor(
    private val mySharedPref: MySharedPref,
    private val authRepository: AuthRepository
) : ProfileDetailViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val firstNameFlow = MutableStateFlow("")

    override val lastNameFlow = MutableStateFlow("")

    override val birthdayFlow = MutableStateFlow("")

    override val phoneFlow = MutableStateFlow("")

    override fun saveData(firstName: String, lastName: String, birthDay: String) {
        viewModelScope.launch {
            loadingSharedFlow.emit(true)
            authRepository.updateUser(firstName, lastName).collectLatest { result ->
                loadingSharedFlow.emit(false)
                result.onSuccess {
                    mySharedPref.firstName = firstName
                    mySharedPref.lastName = lastName
                    mySharedPref.birthDay = birthDay
                }.onMessage {
                    messageSharedFlow.emit(it)
                }.onError {
                    errorSharedFlow.emit(it.getMessage())
                }
            }
        }
    }

    override fun getData() {
        viewModelScope.launch {
            loadingSharedFlow.emit(true)
            firstNameFlow.emit(mySharedPref.firstName)
            lastNameFlow.emit(mySharedPref.lastName)
            birthdayFlow.emit(mySharedPref.birthDay)
            phoneFlow.emit(mySharedPref.phoneNumber)
            loadingSharedFlow.emit(false)
        }
    }
}