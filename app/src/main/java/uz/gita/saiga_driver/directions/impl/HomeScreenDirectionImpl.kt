package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.HomeScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.main.MainScreenDirections
import javax.inject.Inject

class HomeScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : HomeScreenDirection {

    override suspend fun navigateToNotification() {

    }

    override suspend fun navigateToFinance() {
        navigator.navigateTo(MainScreenDirections.actionMainScreenToFinanceScreen())
    }

    override suspend fun navigateToDirectionDetails(orderResponse: OrderResponse) {
        navigator.navigateTo(
            MainScreenDirections.actionMainScreenToDirectionDetailScreen(
                orderResponse.copy(distance = "")
            )
        )
    }

    override suspend fun navigateToAddDirection() {
        navigator.navigateTo(MainScreenDirections.actionMainScreenToAddDirectionScreen())
    }

    override suspend fun navigateToPayment() {
        navigator.navigateTo(MainScreenDirections.actionMainScreenToPaymentScreen())
    }
}