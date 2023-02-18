package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.HomeScreenDirection
import uz.gita.saiga_driver.domain.repository.AuthRepository
import uz.gita.saiga_driver.domain.repository.DirectionsRepository
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.home.HomeViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val orderRepository: OrderRepository,
    private val direction: HomeScreenDirection,
    private val authRepository: AuthRepository,
    private val mySharedPref: MySharedPref,
    private val directionsRepository: DirectionsRepository
) : HomeViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val currentBalanceFlow = MutableStateFlow("")

    override val ordersFlow = MediatorLiveData<Int>()

    override val incomeBalance = MutableStateFlow(0.0)

    override val expanseBalance = MutableStateFlow(0.0)

    override val myDirectionsFlow = MutableStateFlow(emptyList<OrderResponse>())

    override val nameSharedFlow = MutableSharedFlow<String>()

    init {
        viewModelScope.launch {
            orderRepository.getAllOrders()
        }
    }

    override fun getData() {
        viewModelScope.launch {
            nameSharedFlow.emit(mySharedPref.firstName)
        }
    }

    override fun refreshUserBalance() {
        viewModelScope.launch {
            if (hasConnection()) {
                authRepository.topUpBalance(0L).collectLatest { result ->
                    result.onSuccess {
                        currentBalanceFlow.emit(it.balance)
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

    override fun getAllMyDirections() {
        viewModelScope.launch {
            if (hasConnection()) {
                directionsRepository.getAllMyDirections()
                    .collectLatest { result ->
                        result.onSuccess {
                            myDirectionsFlow.emit(it)
                        }.onMessage {
                            messageSharedFlow.emit(it)
                        }.onError {
                            errorSharedFlow.emit(it.getMessage())
                        }
                    }

                ordersFlow.addSource(orderRepository.ordersLiveData) { result ->
                    result.onSuccess {
                        ordersFlow.postValue(it.size)
                    }.onMessage {
                        messageSharedFlow.tryEmit(it)
                    }.onError {
                        errorSharedFlow.tryEmit(it.getMessage())
                    }
                }

                orderRepository.getAllActiveOrders()
            } else {
                messageSharedFlow.emit("No internet connection")
            }
        }
    }

    override fun navigateToOrders() {
        viewModelScope.launch {
            direction.navigateToOrders()
        }
    }

    override fun navigateToFinance() {
        viewModelScope.launch {
            direction.navigateToFinance()
        }
    }

    override fun navigateToDirectionDetail(orderResponse: OrderResponse) {
        viewModelScope.launch {
            direction.navigateToDirectionDetails(orderResponse)
        }
    }

    override fun navigateToNotifications() {
        viewModelScope.launch {
            direction.navigateToNotification()
        }
    }

    override fun navigateToAddDirection() {
        viewModelScope.launch {
            direction.navigateToAddDirection()
        }
    }
}