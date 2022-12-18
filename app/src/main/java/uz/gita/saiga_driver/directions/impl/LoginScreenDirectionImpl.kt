package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.directions.LoginScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.login.LoginScreenDirections
import javax.inject.Inject

class LoginScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : LoginScreenDirection {
    override suspend fun navigateToVerifyScreen(phone: String) {
        navigator.navigateTo(LoginScreenDirections.actionLoginScreenToVerifyCodeScreen())
    }

    override suspend fun navigateToRegisterScreen() {
        navigator.navigateTo(LoginScreenDirections.actionLoginScreenToRegisterScreen())
    }
}