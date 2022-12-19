package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.directions.ProfileScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import javax.inject.Inject

class ProfileScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : ProfileScreenDirection {
    override suspend fun navigateToDirections() {
        //TODO
    }

    override suspend fun navigateToFinance() {
        //TODO
    }

    override suspend fun navigateToSettings() {
        //TODO
    }

    override suspend fun navigateToCustomerCare() {
        //TODO
    }
}