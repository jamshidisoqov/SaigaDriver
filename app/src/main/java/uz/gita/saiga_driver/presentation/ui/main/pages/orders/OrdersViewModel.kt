package uz.gita.saiga_driver.presentation.ui.main.pages.orders

import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/19/2022
interface OrdersViewModel : BaseViewModel {

    val allOrderFlow: StateFlow<List<OrderResponse>>

    fun getAllData()

    fun accept(orderData: OrderResponse)

}