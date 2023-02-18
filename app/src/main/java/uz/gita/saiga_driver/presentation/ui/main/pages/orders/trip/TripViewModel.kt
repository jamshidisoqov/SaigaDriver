package uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 2/5/2023
interface TripViewModel : BaseViewModel {

    val currentSpeed: StateFlow<Int>

    val currentWay: StateFlow<Double>

    val currentMoney: StateFlow<Double>

    val backSharedFlow:SharedFlow<Unit>

    val endOrderDialog:SharedFlow<Unit>

    fun setCurrentLocation(currentLocation: LatLng,isStartTrip:Boolean)

    fun navigateToMap(orderResponse: OrderResponse)

    fun endOrder(orderResponse: OrderResponse)

    fun cancelOrder(orderResponse: OrderResponse)
}