package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.directions.route.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.remote.response.map.DestinationData
import uz.gita.saiga_driver.data.remote.response.map.RouteData
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.presentation.ui.main.pages.orders.map.TripMapViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class TripMapViewModelImpl @Inject constructor(
    private val repository: MapRepository
) : ViewModel(), RoutingListener, TripMapViewModel {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val currentLocation: SharedFlow<LatLng>
        get() = TODO("Not yet implemented")

    override val routesFlow = MutableSharedFlow<RouteData>()

    override fun getRoutesByLocation(destinationData: DestinationData) {
        viewModelScope.launch {
            Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this@TripMapViewModelImpl)
                .waypoints(destinationData.start, destinationData.end)
                .key(uz.gita.saiga_driver.BuildConfig.MAP_API_KEY)
                .build().execute()
        }
    }

    override fun declineOrder(orderResponse: OrderResponse) {
        viewModelScope.launch {

        }
    }

    override fun onRoutingFailure(p0: RouteException?) {
        viewModelScope.launch {
            errorSharedFlow.emit(p0?.getMessage()!!)
        }
    }

    override fun onRoutingStart() {
        viewModelScope.launch {
            loadingSharedFlow.emit(true)
        }
    }

    override fun onRoutingSuccess(p0: ArrayList<Route>?, p1: Int) {
        viewModelScope.launch {
            loadingSharedFlow.emit(false)
            routesFlow.emit(RouteData(p0, p1))
        }
    }

    override fun onRoutingCancelled() {
        viewModelScope.launch {
            loadingSharedFlow.emit(false)
        }
    }

}