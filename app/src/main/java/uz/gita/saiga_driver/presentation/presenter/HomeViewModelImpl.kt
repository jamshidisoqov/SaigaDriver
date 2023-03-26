package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
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
import uz.gita.saiga_driver.utils.extensions.log
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

    override val loadingBalanceLiveData = MutableLiveData<Boolean>()

    override val loadingFinanceLiveData = MutableLiveData<Boolean>()

    override val loadingOrdersLiveData = MutableLiveData<Boolean>()

    override val currentBalanceFlow = MutableLiveData("")

    override val ordersFlow = MediatorLiveData<Int>()

    override val incomeBalance = MutableStateFlow(0.0)

    override val expanseBalance = MutableStateFlow(0.0)

    override val myDirectionsFlow = MutableStateFlow(emptyList<OrderResponse>())

    override val nameSharedFlow = MutableSharedFlow<String>()

    override fun getData() {
        viewModelScope.launch {
            nameSharedFlow.emit(mySharedPref.firstName)
        }
    }

    override fun refreshUserBalance() {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingBalanceLiveData.postValue(true)
                authRepository.getUserBalance().collectLatest { result ->
                    loadingBalanceLiveData.postValue(false)
                    result.onSuccess {
                        log(it.balance.toString())
                        currentBalanceFlow.value = it.balance.toString()
                        incomeBalance.emit(it.benefit)
                        expanseBalance.emit(it.balanceOut)
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
                loadingSharedFlow.emit(true)
                directionsRepository.getAllMyDirections()
                    .collectLatest { result ->
                        loadingSharedFlow.emit(false)
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

    override fun getAllOrders() {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingOrdersLiveData.postValue(true)
                orderRepository.getAllActiveOrders()
                loadingOrdersLiveData.postValue(false)
            } else {
                messageSharedFlow.emit("No internet connection")
            }
        }
    }

    override fun getDriverFinance() {
        viewModelScope.launch {
            loadingFinanceLiveData.postValue(true)
            if (hasConnection()) {
                authRepository.getUserBalance().collectLatest { result ->
                    loadingFinanceLiveData.postValue(false)
                    result.onSuccess {
                        incomeBalance.emit(it.benefit)
                        expanseBalance.emit(it.balanceOut)
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

    override fun navigateToPayment() {
        viewModelScope.launch {
            direction.navigateToPayment()
        }
    }
}