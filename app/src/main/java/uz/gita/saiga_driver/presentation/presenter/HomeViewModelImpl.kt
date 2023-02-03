package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.directions.HomeScreenDirection
import uz.gita.saiga_driver.domain.repository.orders.OrderRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.home.HomeViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val orderRepository: OrderRepository,
    private val direction: HomeScreenDirection
) : HomeViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val currentBalanceFlow = MutableStateFlow(0.0)

    override val ordersFlow = MutableStateFlow(0)

    override val incomeBalance = MutableStateFlow(0.0)

    override val expanseBalance = MutableStateFlow(0.0)

    override val myDirectionsFlow = MutableStateFlow(emptyList<DirectionalTaxiData>())

    override fun getData() {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingSharedFlow.emit(true)
                combine(
                    orderRepository.getBalanceFlow(),
                    orderRepository.getExpanseFlow(),
                    orderRepository.getIncomeFlow(),
                    orderRepository.getOrders(),
                    orderRepository.getAllMyDirections()
                ) { balance, expanse, income, orders, directions ->
                    loadingSharedFlow.emit(false)
                    balance.onSuccess {
                        currentBalanceFlow.emit(it)
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                    expanse.onSuccess {
                        expanseBalance.emit(it)
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                    income.onSuccess {
                        incomeBalance.emit(it)
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                    orders.onSuccess {
                        ordersFlow.emit(it)
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                    directions.onSuccess {
                        myDirectionsFlow.emit(it)
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

    override fun navigateToDirectionDetail(directionalTaxiData: DirectionalTaxiData) {
        viewModelScope.launch {
            direction.navigateToDirectionDetails(directionalTaxiData)
        }
    }

    override fun navigateToNotifications() {
        viewModelScope.launch {
            direction.navigateToNotification()
        }
    }
}