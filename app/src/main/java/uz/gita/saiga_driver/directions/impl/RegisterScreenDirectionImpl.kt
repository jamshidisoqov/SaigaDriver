package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.directions.RegisterScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.register.RegisterScreenDirections
import javax.inject.Inject

class RegisterScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : RegisterScreenDirection {
    override suspend fun navigateToVerify(phone: String) {
        navigator.navigateTo(RegisterScreenDirections.actionRegisterScreenToVerifyCodeScreen())
    }

    override suspend fun navigateToLogin() {
        navigator.navigateTo(RegisterScreenDirections.actionRegisterScreenToLoginScreen())
    }
}