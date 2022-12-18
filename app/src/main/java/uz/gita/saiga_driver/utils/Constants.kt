package uz.gita.saiga_driver.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

// Created by Jamshid Isoqov on 12/18/2022



const val DEBOUNCE_TEXT_CHANGES: Long = 200L
const val DEBOUNCE_VIEW_CLICK: Long = 100L
const val LOCATION_PERMISSION:String = Manifest.permission.ACCESS_FINE_LOCATION
const val LOCATION_COARSE_PERMISSION:String = Manifest.permission.ACCESS_COARSE_LOCATION
const val NOTIFICATION_PERMISSION:String = Manifest.permission.FOREGROUND_SERVICE