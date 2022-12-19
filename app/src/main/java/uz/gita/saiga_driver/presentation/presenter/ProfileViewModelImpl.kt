package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.directions.ProfileScreenDirection
import uz.gita.saiga_driver.presentation.ui.main.pages.profile.ProfileViewModel
import uz.gita.saiga_driver.utils.extensions.combine
import javax.inject.Inject

@HiltViewModel
class ProfileViewModelImpl @Inject constructor(
    private val mySharedPref: MySharedPref,
    private val direction: ProfileScreenDirection
) : ProfileViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val nameFlow = MutableStateFlow("")

    override val phoneFlow = MutableStateFlow("")


    override fun navigateToDirections() {
        viewModelScope.launch {
            direction.navigateToDirections()
        }
    }

    override fun navigateToFinance() {
        viewModelScope.launch {
            direction.navigateToFinance()
        }
    }

    override fun navigateToSettings() {
        viewModelScope.launch {
            direction.navigateToSettings()
        }
    }

    override fun navigateToCustomerCare() {
        viewModelScope.launch {
            direction.navigateToSettings()
        }
    }

    override fun getData() {
        viewModelScope.launch {
            viewModelScope.launch(Dispatchers.IO) {
                loadingSharedFlow.emit(true)
                nameFlow.emit(mySharedPref.firstName.combine(mySharedPref.lastName))
                phoneFlow.emit(mySharedPref.phoneNumber)
                loadingSharedFlow.emit(false)
            }
        }
    }
}