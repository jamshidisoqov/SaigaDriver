package uz.gita.saiga_driver.presentation.ui.main.pages.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/19/2022
interface HomeViewModel : BaseViewModel {

    val currentBalanceFlow: LiveData<String>

    val ordersFlow: MediatorLiveData<Int>

    val incomeBalance: StateFlow<Double>

    val expanseBalance: StateFlow<Double>

    val myDirectionsFlow: StateFlow<List<OrderResponse>>

    val nameSharedFlow:SharedFlow<String>

    fun getData()

    fun refreshUserBalance()

    fun getAllMyDirections()

    fun navigateToFinance()

    fun navigateToDirectionDetail(orderResponse: OrderResponse)

    fun navigateToNotifications()

    fun navigateToAddDirection()

    fun getAllOrders()

}