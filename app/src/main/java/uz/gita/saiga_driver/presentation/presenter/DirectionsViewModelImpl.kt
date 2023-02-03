package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.DirectionsScreenDirection
import uz.gita.saiga_driver.domain.repository.DirectionsRepository
import uz.gita.saiga_driver.presentation.ui.direction.DirectionsViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import javax.inject.Inject

@HiltViewModel
class DirectionsViewModelImpl @Inject constructor(
    private val directions: DirectionsScreenDirection,
    private val repository: DirectionsRepository
) : DirectionsViewModel, ViewModel() {

    override val allDirections = MutableStateFlow<List<OrderResponse>>(emptyList())

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override fun navigateToAddDirections() {
        viewModelScope.launch {
            directions.navigateToAddDirection()
        }
    }

    override fun navigateToDirectionDetail(directionalTaxiData: OrderResponse) {
        viewModelScope.launch {
            directions.navigateToDirectionDetail(directionalTaxiData)
        }
    }

    override fun getAllDirection() {
        viewModelScope.launch {

        }
    }
}