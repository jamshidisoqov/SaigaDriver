package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.directions.MainScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import javax.inject.Inject

class MainScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : MainScreenDirection {
    override suspend fun navigateToNotification() {

    }
}