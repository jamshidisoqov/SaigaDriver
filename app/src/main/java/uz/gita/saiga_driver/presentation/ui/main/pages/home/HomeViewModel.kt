package uz.gita.saiga_driver.presentation.ui.main.pages.home

import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/19/2022
interface HomeViewModel : BaseViewModel {

    val currentBalanceFlow: StateFlow<Double>

    val ordersFlow: StateFlow<Int>

    val incomeBalance: StateFlow<Double>

    val expanseBalance: StateFlow<Double>

    val myDirectionsFlow: StateFlow<List<OrderResponse>>

    fun getData()

    fun refreshUserBalance()

    fun navigateToOrders()

    fun navigateToFinance()

    fun navigateToDirectionDetail(orderResponse: OrderResponse)

    fun navigateToNotifications()

}