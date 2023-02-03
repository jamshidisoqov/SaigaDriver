package uz.gita.saiga_driver.directions.impl

import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.DirectionsScreenDirection
import javax.inject.Inject

class DirectionsScreenDirectionImpl @Inject constructor() : DirectionsScreenDirection {
    override suspend fun navigateToAddDirection() {
        //TODO navigate to add direction
    }

    override suspend fun navigateToDirectionDetail(directionalTaxiData: OrderResponse) {
        //TODO navigate to directional detail
    }
}