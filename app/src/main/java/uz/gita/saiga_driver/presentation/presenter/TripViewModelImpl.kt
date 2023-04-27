package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.data.remote.request.order.EndOrderRequest
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.TripScreenDirection
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.TripViewModel
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.extensions.distance
import uz.gita.saiga_driver.utils.extensions.getFormat
import uz.gita.saiga_driver.utils.extensions.getMessage
import uz.gita.saiga_driver.utils.extensions.getTimeFormat
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class TripViewModelImpl @Inject constructor(
    private val direction: TripScreenDirection,
    private val orderRepository: OrderRepository,
    private val mapRepository: MapRepository,
    private val mySharedPref: MySharedPref
) : TripViewModel, ViewModel() {


    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    private var lastLocation: LatLng? = null

    override val currentWay = MutableStateFlow(0.0)

    override val currentMoney = MutableStateFlow(mySharedPref.minPrice.toDouble())

    override val backSharedFlow = MutableSharedFlow<Unit>()

    override val endOrderDialog = MutableSharedFlow<Unit>()

    override val openGoogleMapSharedFlow = MutableSharedFlow<LatLng>()

    private var _way = 0.0

    private var _money = mySharedPref.minPrice.toDouble()

    private var _price = mySharedPref.minPrice.toDouble()

    private var pauseJob: Job? = null

    override fun setCurrentLocation(currentLocation: LatLng, isStartTrip: Boolean) {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                lastLocation?.let {
                    val way = distance(it, currentLocation)
                    if (isStartTrip) {
                        if (way > 0.003) _way += way
                    }
                    if (_way > 3.0) {
                        val dMoney = (_way - 3.0) * 1000
                        _money = _price + dMoney
                        currentMoney.emit(_money.getFormat(2).toDouble())
                    }
                    currentWay.emit(_way)

                }
                lastLocation = currentLocation
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun navigateToMap(orderResponse: OrderResponse) {
        viewModelScope.launch {
            direction.navigateToMap(orderResponse)
        }
    }

    override fun endOrder(orderResponse: OrderResponse, startTime: Long, endTime: Long) {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingSharedFlow.emit(true)
                orderRepository.endOrder(
                    EndOrderRequest(
                        OrderLengthOfWay = _way,
                        orderId = orderResponse.id,
                        orderMoney = _money,
                        startTime.getTimeFormat(),
                        endTime.getTimeFormat()
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

    override fun openGoogleMap() {
        try {
            viewModelScope.launch {
                if (currentLocation.value != null) {
                    openGoogleMapSharedFlow.emit(currentLocation.value!!)
                } else {
                    if (hasConnection()) {
                        loadingSharedFlow.emit(true)
                        mapRepository.requestCurrentLocation().collectLatest { result ->
                            loadingSharedFlow.emit(false)
                            result.onSuccess {
                                openGoogleMapSharedFlow.emit(it)
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun pauseOrder() {
        pauseJob?.cancel()
        pauseJob = viewModelScope.launch(Dispatchers.IO) {
            delay(120000)
            _money += 2000
            _price += 2000
            currentMoney.emit(_money)
            while (true) {
                delay(60000)
                _money += 500
                _price += 500
                currentMoney.emit(_money)
            }
        }
    }

    override fun resumeOrder() {
        pauseJob?.cancel()
    }
}