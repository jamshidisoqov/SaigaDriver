package uz.gita.saiga_driver.utils

import android.Manifest
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableSharedFlow

// Created by Jamshid Isoqov on 12/18/2022


const val DEBOUNCE_TEXT_CHANGES: Long = 200L
const val DEBOUNCE_VIEW_CLICK: Long = 100L
const val LOCATION_PERMISSION: String = Manifest.permission.ACCESS_FINE_LOCATION
const val LOCATION_COARSE_PERMISSION: String = Manifest.permission.ACCESS_COARSE_LOCATION
val NOTIFICATION_PERMISSION: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    Manifest.permission.FOREGROUND_SERVICE
} else {
    Manifest.permission.CALL_PHONE
}


val NUKUS = LatLng(42.460168, 59.607280)

val currentLocation:MutableLiveData<LatLng> = MutableLiveData()
val currentLocationBearing:MutableLiveData<Pair<LatLng,Float>> = MutableLiveData()
