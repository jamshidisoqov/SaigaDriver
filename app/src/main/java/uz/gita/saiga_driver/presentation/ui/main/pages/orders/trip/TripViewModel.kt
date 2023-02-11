package uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.StateFlow

// Created by Jamshid Isoqov on 2/5/2023
interface TripViewModel {

    val currentSpeed: StateFlow<Int>

    val currentWay: StateFlow<Double>

    val currentMoney: StateFlow<Double>

    fun setCurrentLocation(currentLocation: LatLng)


}