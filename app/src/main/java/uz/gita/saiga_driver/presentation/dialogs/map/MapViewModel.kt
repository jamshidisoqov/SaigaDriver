package uz.gita.saiga_driver.presentation.dialogs.map

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharedFlow
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/16/2022
interface MapViewModel : BaseViewModel {

    val address: SharedFlow<String>

    val currentLocationFlow: LiveData<LatLng>

    fun getAddressByLocation(latLng: LatLng)

    fun requestCurrentLocation()
}