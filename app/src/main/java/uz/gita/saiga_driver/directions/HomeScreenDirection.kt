package uz.gita.saiga_driver.directions

import uz.gita.saiga_driver.data.remote.response.order.OrderResponse

// Created by Jamshid Isoqov on 12/19/2022
interface HomeScreenDirection {

    suspend fun navigateToNotification()

    suspend fun navigateToFinance()

    suspend fun navigateToDirectionDetails(orderResponse: OrderResponse)

    suspend fun navigateToAddDirection()


}