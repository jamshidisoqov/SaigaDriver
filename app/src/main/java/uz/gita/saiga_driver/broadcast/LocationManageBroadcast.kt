package uz.gita.saiga_driver.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager

// Created by Jamshid Isoqov on 2/25/2023
class LocationManageBroadcast : BroadcastReceiver() {

    private var receiverCallBack: ((String, Boolean) -> Unit)? = null

    fun setReceiverCallBack(block: (String, Boolean) -> Unit) {
        receiverCallBack = block
    }

    override fun onReceive(p0: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_PROVIDER_CHANGED -> {
                val locationManager =
                    p0?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                receiverCallBack?.invoke(
                    Intent.ACTION_PROVIDER_CHANGED,
                    isGpsEnabled || isNetworkEnabled
                )
            }
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                val isAirplaneModeOn = intent.getBooleanExtra("state", false)
                receiverCallBack?.invoke(Intent.ACTION_AIRPLANE_MODE_CHANGED, isAirplaneModeOn)
            }
            WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                val action = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                receiverCallBack?.invoke(
                    WifiManager.NETWORK_STATE_CHANGED_ACTION,
                    action?.isConnected!!
                )
            }
        }
    }
}