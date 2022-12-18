package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.directions.VerifyScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.verify.VerifyCodeScreenDirections
import javax.inject.Inject

class VerifyScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : VerifyScreenDirection {
    override suspend fun navigateToPermissionChecker() {
        navigator.navigateTo(VerifyCodeScreenDirections.actionVerifyCodeScreenToPermissionsCheckScreen())
    }
}