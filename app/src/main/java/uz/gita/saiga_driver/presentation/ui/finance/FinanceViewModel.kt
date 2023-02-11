package uz.gita.saiga_driver.presentation.ui.finance

import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.entity.TripWithDate
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 2/6/2023
interface FinanceViewModel : BaseViewModel {

    val allOrdersHistory: StateFlow<List<TripWithDate>>

    fun getAllTrips()

    fun navigateToTripDetails(orderResponse: OrderResponse)

}