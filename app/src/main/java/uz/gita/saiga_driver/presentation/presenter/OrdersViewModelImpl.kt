package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.remote.response.OrderData
import uz.gita.saiga_driver.domain.repository.orders.OrderRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.OrdersViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import javax.inject.Inject

@HiltViewModel
class OrdersViewModelImpl @Inject constructor(
    private val orderRepository: OrderRepository
) : OrdersViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val allOrderFlow = MutableStateFlow<List<OrderData>>(emptyList())

    override fun getAllData() {
        viewModelScope.launch {
            loadingSharedFlow.emit(true)
            orderRepository.getAllOrders().collectLatest { result ->
                loadingSharedFlow.emit(false)
                result.onSuccess {
                    allOrderFlow.emit(it)
                }.onMessage {
                    messageSharedFlow.emit(it)
                }.onError {
                    errorSharedFlow.emit(it.getMessage())
                }
            }
        }
    }

    override fun accept(orderData: OrderData) {
        viewModelScope.launch {

        }
    }
}