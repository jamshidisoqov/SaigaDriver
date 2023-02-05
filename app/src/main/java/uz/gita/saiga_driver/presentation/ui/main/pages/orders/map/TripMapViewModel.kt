package uz.gita.saiga_driver.presentation.ui.main.pages.orders.map

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharedFlow
import uz.gita.saiga_driver.data.remote.response.map.DestinationData
import uz.gita.saiga_driver.data.remote.response.map.RouteData
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 2/5/2023
interface TripMapViewModel : BaseViewModel{

    val currentLocation:SharedFlow<LatLng>

    val routesFlow: SharedFlow<RouteData>

    fun getRoutesByLocation(destinationData: DestinationData)

    fun declineOrder(orderResponse: OrderResponse)

}