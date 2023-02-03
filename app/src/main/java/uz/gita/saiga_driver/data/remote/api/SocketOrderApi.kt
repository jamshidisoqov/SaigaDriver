package uz.gita.saiga_driver.data.remote.api

import kotlinx.coroutines.flow.Flow
import me.adkhambek.moon.Event
import uz.gita.saiga_driver.data.remote.request.order.OrderRequest
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse

// Created by Jamshid Isoqov on 2/2/2023
interface SocketOrderApi {

    @Event(value = "/chat")
    suspend fun buyOrder(orderRequest: OrderRequest)

    @Event(value = "/chat")
    fun getAllOrders():Flow<List<OrderResponse>>

}