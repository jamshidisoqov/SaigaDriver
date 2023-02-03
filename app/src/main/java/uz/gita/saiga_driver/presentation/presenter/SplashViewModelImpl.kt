package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.directions.SplashScreenDirection
import uz.gita.saiga_driver.domain.enums.StartScreen
import uz.gita.saiga_driver.domain.repository.AuthRepository
import uz.gita.saiga_driver.presentation.ui.splash.SplashViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val direction: SplashScreenDirection
) : SplashViewModel, ViewModel() {
    override fun navigateToScreen() {
        viewModelScope.launch {
            delay(1000L)
            when (authRepository.getStartScreen()) {
                StartScreen.MAIN -> direction.navigateToMain()
                StartScreen.LOGIN -> direction.navigateToLogin()
            }
        }
    }
}