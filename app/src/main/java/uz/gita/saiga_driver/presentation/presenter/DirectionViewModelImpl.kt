package uz.gita.saiga_driver.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.directions.route.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.saiga_driver.data.remote.response.map.RouteData
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.repository.DirectionsRepository
import uz.gita.saiga_driver.presentation.ui.direction.detail.DirectionViewModel
import uz.gita.saiga_driver.utils.extensions.getMessage
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class DirectionViewModelImpl @Inject constructor(
    private val directionsRepository: DirectionsRepository
) : DirectionViewModel, RoutingListener,
    OnConnectionFailedListener, ViewModel() {

    override val loadingSharedFlow = MutableSharedFlow<Boolean>()

    override val messageSharedFlow = MutableSharedFlow<String>()

    override val errorSharedFlow = MutableSharedFlow<String>()

    override val backSharedFlow = MutableSharedFlow<Unit>()

    override val routes = MutableSharedFlow<RouteData>()


    override fun delete(directionalTaxiData: OrderResponse) {
        viewModelScope.launch {
            if (hasConnection()) {
                loadingSharedFlow.emit(true)
                directionsRepository.cancelOrder(directionalTaxiData.id)
                    .collectLatest { result ->
                        loadingSharedFlow.emit(false)
                        result.onSuccess {
                            backSharedFlow.emit(Unit)
                        }.onMessage {
                            messageSharedFlow.emit(it)
                        }.onError {
                            errorSharedFlow.emit(it.getMessage())
                        }
                    }
            } else {
                messageSharedFlow.emit("no internet connection")
            }
        }
    }

    override fun findRoutes(directionalTaxiData: OrderResponse) {
        val from = directionalTaxiData.direction.addressFrom
        val to = directionalTaxiData.direction.addressTo
        val routeData = Routing.Builder()
            .travelMode(AbstractRouting.TravelMode.DRIVING)
            .withListener(this)
            .alternativeRoutes(true)
            .waypoints(
                LatLng(from.lat ?: 21.0, from.lon ?: 23.32),
                LatLng(to?.lat ?: 21.0, to?.lon ?: 23.32),
            )
            .key(uz.gita.saiga_driver.BuildConfig.MAP_API_KEY)
            .build()
        routeData.execute()
    }


    override fun onRoutingFailure(p0: RouteException?) {
        viewModelScope.launch {
            errorSharedFlow.emit(p0?.getMessage() ?: "Unknown error")
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
            routes.emit(RouteData(p0, p1))
        }
    }

    override fun onRoutingCancelled() {
        viewModelScope.launch {
            loadingSharedFlow.emit(false)
            messageSharedFlow.emit("Cancelled")
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        viewModelScope.launch {
            loadingSharedFlow.emit(false)
            errorSharedFlow.emit(p0.errorMessage ?: "No internet connection")
        }
    }
}