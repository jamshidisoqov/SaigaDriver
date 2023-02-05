package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import uz.gita.saiga_driver.data.remote.response.auth.UserResponse
import uz.gita.saiga_driver.data.remote.response.auth.enums.Status
import uz.gita.saiga_driver.data.remote.response.order.AddressResponse
import uz.gita.saiga_driver.data.remote.response.order.DirectionResponse
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.OrderPageDirections
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.OrdersViewModel
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.extensions.calculationByDistance
import uz.gita.saiga_driver.utils.extensions.getFormat
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class OrdersViewModelImpl @Inject constructor(
    private val orderRepository: OrderRepository,
    private val mapRepository: MapRepository,
    private val directions: OrderPageDirections
) : OrdersViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val allOrderFlow = MutableStateFlow<List<OrderResponse>>(emptyList())

    override val currentLocationFlow = MutableLiveData<LatLng>()

    private var currentLocation: LatLng = NUKUS

    init {
        //TODO delete
        viewModelScope.launch {
            val orderList = arrayListOf<OrderResponse>()
            for (i in 0..100) {
                delay(2000)
                orderList.add(
                    OrderResponse(
                        id = i.toLong(),
                        fromUser = UserResponse(
                            i.toLong(),
                            "+99890",
                            "Jamshid",
                            "Isoqov",
                            Status.ACTIVE
                        ),
                        123.0,
                        DirectionResponse(
                            1,
                            AddressResponse(1, 42.460168, 59.607280, "Nukus"),
                            null
                        )
                    )
                )
                allOrderFlow.emit(orderList.toMutableList())
                updateDistances(currentLocation)
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(5000L)
                mapRepository.requestCurrentLocation()
                    .collectLatest { result ->
                        result.onSuccess {
                            currentLocation = it
                            updateDistances(currentLocation)
                        }
                    }
            }
        }
    }

    private suspend fun updateDistances(currentLocation: LatLng) {
        viewModelScope.launch {
            val orders = allOrderFlow.value.toMutableList()
            val distances = mutableListOf<Deferred<Double>>()
            orders.forEach { orderResponse ->
                val direction = orderResponse.direction.addressFrom
                distances.add(async {
                    calculationByDistance(
                        currentLocation,
                        LatLng(direction.lat!!, direction.lon!!)
                    )
                })
            }
            for (index in orders.indices) {
                orders[index] = orders[index].copy(distance = distances[index].await().getFormat(2))
            }
            allOrderFlow.emit(orders)
        }
    }

    override fun getAllData() {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingSharedFlow.emit(true)
                orderRepository.getAllOrders().collectLatest { result ->
                    allOrderFlow.emit(result)
                }
            }
        }
    }

    override fun accept(orderData: OrderResponse) {
        viewModelScope.launch {
            directions.navigateToTrip(orderData)
        }
    }
}