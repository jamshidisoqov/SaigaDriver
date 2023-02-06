package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.DirectionsScreenDirection
import uz.gita.saiga_driver.navigation.Navigator
import uz.gita.saiga_driver.presentation.ui.direction.DirectionsScreenDirections
import javax.inject.Inject

class DirectionsScreenDirectionImpl @Inject constructor(
    private val navigation: Navigator
) : DirectionsScreenDirection {
    override suspend fun navigateToAddDirection() {
        navigation.navigateTo(DirectionsScreenDirections.actionDirectionsScreenToAddDirectionScreen())
    }

    override suspend fun navigateToDirectionDetail(directionalTaxiData: OrderResponse) {
       navigation.navigateTo(DirectionsScreenDirections.actionDirectionsScreenToDirectionDetailScreen(directionalTaxiData))
    }
}