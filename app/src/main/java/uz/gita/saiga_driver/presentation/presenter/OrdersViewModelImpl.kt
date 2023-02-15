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
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.OrderPageDirections
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.OrdersViewModel
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.extensions.calculationByDistance
import uz.gita.saiga_driver.utils.extensions.getFormat
import uz.gita.saiga_driver.utils.extensions.getMessage
import uz.gita.saiga_driver.utils.extensions.log
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class OrdersViewModelImpl @Inject constructor(
    private val orderRepository: OrderRepository,
    private val directions: OrderPageDirections,
    private val mapRepository: MapRepository
) : OrdersViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val allOrderFlow = MutableStateFlow<List<OrderResponse>>(emptyList())

    override val currentLocationFlow = MutableLiveData<LatLng>()

    private var currentLocation: LatLng = NUKUS

    init {
        viewModelScope.launch {
            delay(1000)
            mapRepository.requestCurrentLocation().collectLatest {
                it.onSuccess {
                    updateDistances(it)
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
                        LatLng(direction.lat ?: NUKUS.latitude, direction.lon ?: NUKUS.longitude)
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
                    result.onSuccess {
                        allOrderFlow.emit(it)
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                }
            } else {
                messageSharedFlow.emit("No internet connection")
            }
        }
    }

    override fun setCurrentLocation(currentLocation: LatLng) {
        viewModelScope.launch(Dispatchers.Default) {
            val distance =
                calculationByDistance(currentLocation, this@OrdersViewModelImpl.currentLocation)
            log(distance.toString(), "VVV")
            if (distance > 0.03) updateDistances(currentLocation)
            this@OrdersViewModelImpl.currentLocation = currentLocation
        }
    }

    override fun accept(orderData: OrderResponse) {
        viewModelScope.launch {
            if (hasConnection()) {
                //loadingSharedFlow.emit(true)
                directions.navigateToTrip(orderData)
                orderRepository.receiveOrder(orderData.id)
                    .collectLatest { result ->
                        loadingSharedFlow.emit(false)
                        result.onSuccess {
                            directions.navigateToTrip(it)
                        }.onMessage {
                            messageSharedFlow.emit(it)
                        }.onError {
                            errorSharedFlow.emit(it.getMessage())
                        }
                    }
            } else {
                messageSharedFlow.emit("No internet connection")
            }
        }
    }
}