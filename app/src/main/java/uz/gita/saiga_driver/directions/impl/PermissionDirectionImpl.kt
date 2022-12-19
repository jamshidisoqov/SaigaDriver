package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.directions.PermissionDirection
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.permission.PermissionsCheckScreenDirections
import javax.inject.Inject

class PermissionDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : PermissionDirection {
    override suspend fun navigateToMain() {
        navigator.navigateTo(PermissionsCheckScreenDirections.actionPermissionsCheckScreenToMainScreen())
    }
}