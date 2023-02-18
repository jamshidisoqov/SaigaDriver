package uz.gita.saiga_driver.services.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.broadcast.OrderBroadcast
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.OrderStatus
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.extensions.calculationByDistance
import uz.gita.saiga_driver.utils.extensions.fromJsonData
import uz.gita.saiga_driver.utils.extensions.toJsonData
import javax.inject.Inject

// Created by Jamshid Isoqov on 2/17/2023
@AndroidEntryPoint
class NotificationService @Inject constructor() : Service() {

    private var scope = CoroutineScope(Dispatchers.IO + Job())

    @Inject
    lateinit var mapRepository: MapRepository

    private lateinit var orderBroadcast: OrderBroadcast

    private lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var gson: Gson

    private var channelId = 1

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        orderBroadcast = OrderBroadcast()
        this.registerReceiver(orderBroadcast, IntentFilter().apply {
            addAction(OrderStatus.ACCEPT.name)
            addAction(OrderStatus.BACK.name)
        })
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val data = intent.getStringExtra("order")
            if (data != null) {
                val orderResponse = gson.fromJsonData<OrderResponse>(
                    data,
                    object : TypeToken<OrderResponse>() {}.type
                )
                createNotification(orderResponse)
            }
        }
        return START_REDELIVER_INTENT

    }

    @SuppressLint("MissingPermission")
    private fun createNotification(orderResponse: OrderResponse) {

        val accept =
            Intent(baseContext, orderBroadcast.javaClass).setAction(OrderStatus.ACCEPT.name)
        accept.putExtra("order",gson.toJsonData(orderResponse))
        val acceptPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            accept,
            PendingIntent.FLAG_IMMUTABLE
        )

        val back =
            Intent(baseContext, orderBroadcast.javaClass).setAction(OrderStatus.BACK.name)
        val backPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            accept,
            PendingIntent.FLAG_IMMUTABLE
        )

        scope.launch {
            val fromAddress = with(orderResponse.direction.addressFrom) {
                LatLng(lat!!, lon!!)
            }
            mapRepository.getAddressByLocation(fromAddress).collectLatest {
                it.onSuccess { direction ->

                    val notification =
                        NotificationCompat.Builder(this@NotificationService, "Order")
                            .apply {
                                setSmallIcon(R.drawable.saiga_logo)
                                setContentTitle(direction)
                                setContentText(
                                    "Distance ${
                                        calculationByDistance(
                                            fromAddress,
                                            currentLocation.value ?: NUKUS
                                        )
                                    }"
                                )
                                addAction(R.drawable.ic_accept, "Accept", acceptPendingIntent)
                                setAutoCancel(true)
                            }
                            .build()
                    notificationManager.notify(channelId++, notification)
                }
            }
        }

    }

    private fun createNotificationChannel() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Order"
            val descriptionText = "This channel user notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Order", name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }


}