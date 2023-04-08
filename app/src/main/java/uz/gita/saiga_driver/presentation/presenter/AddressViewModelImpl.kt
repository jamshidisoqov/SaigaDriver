package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.data.remote.response.order.AddressResponse
import uz.gita.saiga_driver.domain.repository.DirectionsRepository
import uz.gita.saiga_driver.presentation.ui.direction.add.address.AddressViewModel
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class AddressViewModelImpl @Inject constructor(
    private val directionsRepository: DirectionsRepository,
    private val mySharedPref: MySharedPref
) : AddressViewModel, ViewModel() {

    private var allAddress = arrayListOf<AddressResponse>()


    override val allAddressFlow = MutableStateFlow<List<AddressResponse>>(emptyList())

    private var searchJob: Job? = null

    override fun searchAddress(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.Default) {
            delay(300)
            if (hasConnection()) {
                directionsRepository.searchAddress(query).collectLatest { result ->
                    result.onSuccess {
                        allAddressFlow.emit(it.map { searchResponse ->
                            searchResponse.toAddressResponse(
                                mySharedPref.getCurrentLatLng()
                            )
                        })
                    }
                }
            }
        }
    }
}