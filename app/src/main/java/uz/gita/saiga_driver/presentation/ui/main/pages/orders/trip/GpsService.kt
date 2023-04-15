package uz.gita.saiga_driver.presentation.ui.main.pages.orders.trip

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.currentLocationBearing
import uz.gita.saiga_driver.utils.extensions.log


// Created by Jamshid Isoqov on 2/5/2023
class GpsService : Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    override fun onBind(p0: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(1000L)
            .setIntervalMillis(1000L)
            .setMinUpdateDistanceMeters(0f)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    location.lastLocation?.let { p0 ->
                        val latLng = LatLng(p0.latitude, p0.longitude)
                        log("Last location->$latLng")
                        currentLocation.value = latLng
                        if (p0.bearing > 0.01) currentLocationBearing.value =
                            Pair(latLng, if (p0.bearing > 0.01) p0.bearing else 0f)
                    }
                }
            },
            null
        )
    }
}