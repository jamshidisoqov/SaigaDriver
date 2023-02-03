package uz.gita.saiga_driver.directions

import uz.gita.saiga_driver.data.remote.response.order.OrderResponse

// Created by Jamshid Isoqov on 12/19/2022
interface DirectionsScreenDirection {

    suspend fun navigateToAddDirection()

    suspend fun navigateToDirectionDetail(directionalTaxiData: OrderResponse)

}