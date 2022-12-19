package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.directions.PermissionDirection
import uz.gita.saiga_driver.presentation.ui.permission.PermissionsViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModelImpl @Inject constructor(
    private val permissionDirection: PermissionDirection
) : PermissionsViewModel, ViewModel() {
    override fun navigateToMain() {
        viewModelScope.launch {
            permissionDirection.navigateToMain()
        }
    }
}