package uz.gita.saiga_driver.utils

import android.Manifest
import com.google.android.gms.maps.model.LatLng

// Created by Jamshid Isoqov on 12/18/2022


const val DEBOUNCE_TEXT_CHANGES: Long = 200L
const val DEBOUNCE_VIEW_CLICK: Long = 100L
const val LOCATION_PERMISSION: String = Manifest.permission.ACCESS_FINE_LOCATION
const val LOCATION_COARSE_PERMISSION: String = Manifest.permission.ACCESS_COARSE_LOCATION
const val NOTIFICATION_PERMISSION: String = Manifest.permission.FOREGROUND_SERVICE


val NUKUS = LatLng(42.460168, 59.607280)
