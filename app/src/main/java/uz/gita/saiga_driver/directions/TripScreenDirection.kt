package uz.gita.saiga_driver.directions

import uz.gita.saiga_driver.data.remote.response.order.OrderResponse

// Created by Jamshid Isoqov on 2/11/2023
interface TripScreenDirection {
    suspend fun navigateToMap(orderResponse: OrderResponse)
}