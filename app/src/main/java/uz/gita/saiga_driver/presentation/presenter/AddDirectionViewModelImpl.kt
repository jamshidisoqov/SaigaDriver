package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.remote.response.order.DirectionResponse
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.presentation.ui.direction.add.AddDirectionViewModel
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.extensions.getMessage
import uz.gita.saiga_driver.utils.extensions.getTimeWhenFormat
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class AddDirectionViewModelImpl @Inject constructor(
    private val orderRepository: OrderRepository
) : AddDirectionViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val backFlow = MutableSharedFlow<Unit>()

    override val allDirections = MutableStateFlow<List<DirectionResponse>>(emptyList())


    override fun addDirection(
        whereFrom: Pair<String, LatLng?>,
        whereTo: Pair<String, LatLng?>,
        price: Double,
        schedule: String?,
        comment: String?
    ) {

        viewModelScope.launch {
            if (hasConnection()) {
                orderRepository.order(
                    whereFrom = whereFrom.first,
                    whereFromLatLng = whereFrom.second ?: NUKUS,
                    whereTo = whereTo.first,
                    whereToLatLng = whereTo.second,
                    price = price,
                    schedule = schedule?.getTimeWhenFormat(),
                    comment = comment
                ).collectLatest { result ->
                    result.onSuccess {
                        backFlow.emit(Unit)
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