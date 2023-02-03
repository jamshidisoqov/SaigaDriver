package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.domain.repository.directions.DirectionsRepository
import uz.gita.saiga_driver.presentation.ui.direction.add.AddDirectionViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import java.sql.Timestamp
import javax.inject.Inject

@HiltViewModel
class AddDirectionViewModelImpl @Inject constructor(
    private val directionsRepository: DirectionsRepository
) : AddDirectionViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val backFlow = MutableSharedFlow<Unit>()

    override val allDirections = MutableStateFlow<List<AddressData>>(emptyList())

    init {
        viewModelScope.launch {
            directionsRepository.getAllExistsAddress().collectLatest { result->
                result.onSuccess {
                    allDirections.emit(it)
                }.onMessage {
                    messageSharedFlow.emit(it)
                }.onError {
                    errorSharedFlow.emit(it.getMessage())
                }
            }
        }
    }

    override fun addDirection(
        whereFrom: String,
        whereTo: String,
        schedule: Timestamp,
        price: Double
    ) {
        viewModelScope.launch {
            loadingSharedFlow.emit(true)
            directionsRepository.addDirection(
                DirectionalTaxiDto(
                    directionsDto = DirectionsDto(
                        addressFrom = AddressDto(title = whereFrom),
                        addressTo = AddressDto(title = whereTo)
                    ),
                    scheduleTime = schedule,
                    price = price
                )
            ).collectLatest { result ->
                loadingSharedFlow.emit(false)
                result.onSuccess {
                    backFlow.emit(Unit)
                }.onMessage {
                    messageSharedFlow.emit(it)
                }.onError {
                    errorSharedFlow.emit(it.getMessage())
                }
            }
        }
    }
}