package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.directions.TripScreenDirection
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip.TripViewModel
import uz.gita.saiga_driver.utils.extensions.calculationByDistance
import javax.inject.Inject

@HiltViewModel
class TripViewModelImpl @Inject constructor(
    private val direction: TripScreenDirection
) : TripViewModel, ViewModel() {

    private var lastLocation: LatLng? = null

    override val currentSpeed = MutableStateFlow(0)

    override val currentWay = MutableStateFlow(0.0)

    override val currentMoney = MutableStateFlow(7000.0)

    private var _way = 0.0
    private var _money = 7000.0

    override fun setCurrentLocation(currentLocation: LatLng, isStartTrip: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            lastLocation?.let {
                val way = calculationByDistance(it, currentLocation)
                if (isStartTrip) {
                    if (way > 0.003)
                        _way += way
                }
                val speed: Double = way * 1000 * 3.6
                if (_way > 5.0) {
                    val dMoney = (_way - 5.0) * 1000
                    _money += dMoney
                    currentMoney.emit(_money)
                }
                currentWay.emit(_way)

                currentSpeed.emit(speed.toInt())
            }
            lastLocation = currentLocation
        }
    }

    override fun navigateToMap(orderResponse: OrderResponse) {
        viewModelScope.launch {
            direction.navigateToMap(orderResponse)
        }
    }
}