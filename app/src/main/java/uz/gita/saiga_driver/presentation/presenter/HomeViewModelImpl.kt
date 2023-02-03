package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.HomeScreenDirection
import uz.gita.saiga_driver.domain.repository.AuthRepository
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.home.HomeViewModel
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val orderRepository: OrderRepository,
    private val direction: HomeScreenDirection,
    private val authRepository: AuthRepository
) : HomeViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val currentBalanceFlow = MutableStateFlow(0.0)

    override val ordersFlow = MutableStateFlow(0)

    override val incomeBalance = MutableStateFlow(0.0)

    override val expanseBalance = MutableStateFlow(0.0)

    override val myDirectionsFlow = MutableStateFlow(emptyList<OrderResponse>())

    override fun getData() {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingSharedFlow.emit(true)
            } else {
                messageSharedFlow.emit("No internet connection")
            }
        }
    }

    override fun refreshUserBalance() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getUserData().collectLatest { result ->
                result.onSuccess {

                }.onMessage {

                }.onError {

                }
            }

            orderRepository.getAllOrders().collectLatest {
                ordersFlow.emit(it.size)
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
}