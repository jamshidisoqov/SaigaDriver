package uz.gita.saiga_driver.domain.entity

import uz.gita.saiga_driver.data.remote.response.auth.UserResponse
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse

// Created by Jamshid Isoqov on 2/18/2023
data class ReceivedOrderResponse(
    val data: ReceivedResponse,
    val order: OrderResponse
)

data class ReceivedResponse(
    val user: UserResponse,
    val balance: String
)
