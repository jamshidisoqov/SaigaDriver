package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.OrderPageDirections
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.OrdersViewModel
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.extensions.calculationByDistance
import uz.gita.saiga_driver.utils.extensions.getMessage
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

    override val openOrderDialog = MutableSharedFlow<OrderResponse>()

    override val allOrderFlow = MutableStateFlow<List<OrderResponse>>(emptyList())

    override val currentLocationFlow = MediatorLiveData<LatLng>()

    private var currentLocation: LatLng = NUKUS

    init {
        viewModelScope.launch {
            delay(2000)
            mapRepository.requestCurrentLocation().collectLatest {
                it.onSuccess {latLng-> updateDistances(latLng) }
            }
        }
    }

    private suspend fun updateDistances(currentLocation: LatLng?) {
        viewModelScope.launch {
            if (currentLocation != null) {
                val orders = allOrderFlow.value.map {
                    val addressFrom = it.direction.addressFrom
                    it.copy(
                        distance = calculationByDistance(
                            LatLng(
                                addressFrom.lat ?: NUKUS.latitude,
                                addressFrom.lon ?: NUKUS.longitude
                            ), currentLocation
                        ).toString()
                    )
                }.sortedBy { it.distance.toDouble() }.toMutableList()
                if (orders.isNotEmpty()) {
                    if (orders[0].distance.toDouble() < 1.0) {
                        openOrderDialog.emit(orders[0])
                    }
                }
                allOrderFlow.emit(orders)
            }
        }
    }

    override fun getAllData() {
        viewModelScope.launch {
            if (hasConnection()) {
                currentLocationFlow.addSource(orderRepository.ordersLiveData) { result ->
                    result.onSuccess {
                        allOrderFlow.tryEmit(it)
                        viewModelScope.launch {
                            updateDistances(currentLocation)
                        }
                    }.onMessage {
                        messageSharedFlow.tryEmit(it)
                    }.onError {
                        errorSharedFlow.tryEmit(it.getMessage())
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
            if (distance > 0.03) updateDistances(currentLocation)
            this@OrdersViewModelImpl.currentLocation = currentLocation
        }
    }

    override fun accept(orderData: OrderResponse) {
        viewModelScope.launch {
            if (hasConnection()) {
                orderRepository.receiveOrder(orderData.id)
                    .collectLatest { result ->
                        loadingSharedFlow.emit(false)
                        result.onSuccess {
                            directions.navigateToTrip(orderData)
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

    override fun getAllOrders() {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingSharedFlow.emit(true)
                orderRepository.getAllActiveOrders()
                loadingSharedFlow.emit(false)
            } else {
                messageSharedFlow.emit("No internet connection")
            }
        }
    }
}