package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.HomeScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.main.MainScreenDirections
import javax.inject.Inject

class HomeScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : HomeScreenDirection {

    override suspend fun navigateToOrders() {

    }

    override suspend fun navigateToNotification() {

    }

    override suspend fun navigateToFinance() {

    }

    override suspend fun navigateToDirectionDetails(orderResponse: OrderResponse) {

    }

    override suspend fun navigateToAddDirection() {
        navigator.navigateTo(MainScreenDirections.actionMainScreenToAddDirectionScreen())
    }
}