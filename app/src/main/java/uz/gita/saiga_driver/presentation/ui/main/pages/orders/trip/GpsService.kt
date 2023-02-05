package uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import com.google.android.gms.maps.model.LatLng
import uz.gita.saiga_driver.utils.currentLocation

// Created by Jamshid Isoqov on 2/5/2023
class GpsService : Service(), LocationListener {
    var locationManager: LocationManager? = null
    override fun onBind(p0: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("---------------------Start---------------------------")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L,
            0f,
            this
        )
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onLocationChanged(p0: Location) {
        println("TTT-----------------------")
        currentLocation.value = LatLng(p0.latitude, p0.longitude)
    }

}