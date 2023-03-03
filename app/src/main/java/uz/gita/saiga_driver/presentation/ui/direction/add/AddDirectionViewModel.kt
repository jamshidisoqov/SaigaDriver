package uz.gita.saiga_driver.presentation.ui.direction.add

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.data.remote.response.StaticAddressResponse
import uz.gita.saiga_driver.data.remote.response.order.DirectionResponse
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/20/2022
interface AddDirectionViewModel : BaseViewModel {

    val backFlow: SharedFlow<Unit>

    val allDirections: StateFlow<List<StaticAddressResponse>>

    fun addDirection(
        whereFrom: Pair<String, LatLng?>,
        whereTo: Pair<String, LatLng?>,
        price: Double,
        schedule: String?,
        comment: String?
    )

    fun getAllStaticAddress()

}