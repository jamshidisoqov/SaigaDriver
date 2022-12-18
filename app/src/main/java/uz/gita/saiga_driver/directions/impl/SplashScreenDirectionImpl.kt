package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.directions.SplashScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.splash.SplashScreenDirections
import javax.inject.Inject

class SplashScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : SplashScreenDirection {
    override suspend fun navigateToMain() {
        navigator.navigateTo(SplashScreenDirections.actionSplashScreenToMainScreen())
    }

    override suspend fun navigateToLogin() {
        navigator.navigateTo(SplashScreenDirections.actionSplashScreenToLoginScreen())
    }
}