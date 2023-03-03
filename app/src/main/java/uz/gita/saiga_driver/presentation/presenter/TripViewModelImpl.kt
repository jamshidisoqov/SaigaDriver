package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.remote.request.order.EndOrderRequest
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.TripScreenDirection
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.TripViewModel
import uz.gita.saiga_driver.utils.extensions.calculationByDistance
import uz.gita.saiga_driver.utils.extensions.distance
import uz.gita.saiga_driver.utils.extensions.getMessage
import uz.gita.saiga_driver.utils.extensions.log
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class TripViewModelImpl @Inject constructor(
    private val direction: TripScreenDirection,
    private val orderRepository: OrderRepository
) : TripViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    private var lastLocation: LatLng? = null

    override val currentSpeed = MutableStateFlow(0)

    override val currentWay = MutableStateFlow(0.0)

    override val currentMoney = MutableStateFlow(7000.0)

    override val backSharedFlow = MutableSharedFlow<Unit>()

    override val endOrderDialog = MutableSharedFlow<Unit>()

    private var _way = 0.0
    private var _money = 7000.0

    override fun setCurrentLocation(currentLocation: LatLng, isStartTrip: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            lastLocation?.let {
                val way = distance(it, currentLocation)
                if (isStartTrip) {
                    if (way > 0.003)
                        _way += way
                }
                log(way.toString())
                val speed: Double = way * 1000 * 3.6
                if (_way > 3.0) {
                    val dMoney = (_way - 3.0) * 1000
                    _money += dMoney
                    currentMoney.emit(_money)
                }
                currentWay.emit(_way)

                currentSpeed.emit(speed.toInt())
            }
            lastLocation = currentLocation
        }
    }

    override fun navigateToMap(orderResponse: OrderResponse) {
        viewModelScope.launch {
            direction.navigateToMap(orderResponse)
        }
    }

    override fun endOrder(orderResponse: OrderResponse) {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingSharedFlow.emit(true)
                orderRepository.endOrder(
                    EndOrderRequest(
                        OrderLengthOfWay = _way,
                        orderId = orderResponse.id,
                        orderMoney = _money
                    )
                ).collectLatest { result ->
                    loadingSharedFlow.emit(false)
                    result.onSuccess {
                        endOrderDialog.emit(Unit)
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                }
            } else {
                errorSharedFlow.emit("No internet connection")
            }
        }
    }

    override fun cancelOrder(orderResponse: OrderResponse) {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingSharedFlow.emit(true)
                orderRepository.cancelOrder(orderResponse.id).collectLatest { result ->
                    loadingSharedFlow.emit(false)
                    result.onSuccess {
                        backSharedFlow.emit(Unit)
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                }
            } else {
                errorSharedFlow.emit("No internet connection")
            }
        }
    }


}