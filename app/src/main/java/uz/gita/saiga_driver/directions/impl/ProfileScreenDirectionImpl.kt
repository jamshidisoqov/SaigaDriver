package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.directions.ProfileScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.main.MainScreenDirections
import javax.inject.Inject

class ProfileScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : ProfileScreenDirection {
    override suspend fun navigateToDirections() {
        navigator.navigateTo(MainScreenDirections.actionMainScreenToDirectionsScreen())
    }

    override suspend fun navigateToFinance() {
        navigator.navigateTo(MainScreenDirections.actionMainScreenToFinanceScreen())
    }

    override suspend fun navigateToSettings() {
        navigator.navigateTo(MainScreenDirections.actionMainScreenToSettingsScreen())
    }

    override suspend fun navigateToCustomerCare() {
        navigator.navigateTo(MainScreenDirections.actionMainScreenToCustomerCareScreen())
    }
}