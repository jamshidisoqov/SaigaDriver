package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.remote.response.order.DirectionResponse
import uz.gita.saiga_driver.domain.repository.DirectionsRepository
import uz.gita.saiga_driver.presentation.ui.direction.add.AddDirectionViewModel
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

    override val allDirections = MutableStateFlow<List<DirectionResponse>>(emptyList())

    init {
        viewModelScope.launch {

        }
    }

    override fun addDirection(
        whereFrom: String,
        whereTo: String,
        schedule: Timestamp,
        price: Double
    ) {
        viewModelScope.launch {

        }
    }
}