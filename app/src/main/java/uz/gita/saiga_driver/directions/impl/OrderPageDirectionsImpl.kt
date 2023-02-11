package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.OrderPageDirections
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.main.MainScreenDirections
import javax.inject.Inject

class OrderPageDirectionsImpl @Inject constructor(
    private val navigator: Navigator
) : OrderPageDirections {
    override suspend fun navigateToTrip(orderResponse: OrderResponse) {
        navigator.navigateTo(MainScreenDirections.actionMainScreenToTripScreen(orderResponse))
    }
}