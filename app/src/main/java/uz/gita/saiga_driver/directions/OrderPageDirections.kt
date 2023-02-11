package uz.gita.saiga_driver.directions

import uz.gita.saiga_driver.data.remote.response.order.OrderResponse

// Created by Jamshid Isoqov on 2/5/2023
interface OrderPageDirections {

    suspend fun navigateToTrip(orderResponse: OrderResponse)

}