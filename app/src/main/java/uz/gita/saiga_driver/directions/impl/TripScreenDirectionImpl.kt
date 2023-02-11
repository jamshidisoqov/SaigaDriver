package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.TripScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.TripScreenDirections
import javax.inject.Inject

class TripScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
): TripScreenDirection {
    override suspend fun navigateToMap(orderResponse: OrderResponse) {
        navigator.navigateTo(TripScreenDirections.actionTripScreenToTripMapScreen(orderResponse))
    }
}