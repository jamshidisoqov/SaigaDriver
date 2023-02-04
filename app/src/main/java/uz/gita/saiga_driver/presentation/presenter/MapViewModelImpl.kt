package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.presentation.dialogs.map.MapViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class MapViewModelImpl @Inject constructor(
    private val mapRepository: MapRepository
) : MapViewModel, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val address = MutableSharedFlow<String>()

    override val currentLocationFlow = MutableLiveData<LatLng>()

    override fun getAddressByLocation(latLng: LatLng) {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingSharedFlow.emit(true)
                mapRepository.getAddressByLocation(latLng)
                    .collectLatest { result ->
                        result.onSuccess {
                            address.emit(it)
                        }.onMessage {
                            messageSharedFlow.emit(it)
                        }.onError {
                            errorSharedFlow.emit(it.getMessage())
                        }
                        loadingSharedFlow.emit(false)
                    }
            } else {
                messageSharedFlow.emit("No internet connection")
            }
        }
    }

    override fun requestCurrentLocation() {
        viewModelScope.launch {
            loadingSharedFlow.emit(true)
            mapRepository.requestCurrentLocation()
                .collectLatest { result ->
                    loadingSharedFlow.emit(false)
                    result.onSuccess {
                        currentLocationFlow.postValue(it)
                    }.onMessage {
                        messageSharedFlow.emit(it)
                    }.onError {
                        errorSharedFlow.emit(it.getMessage())
                    }
                }
        }
    }
}