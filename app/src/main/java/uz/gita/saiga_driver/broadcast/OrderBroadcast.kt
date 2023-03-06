package uz.gita.saiga_driver.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.saiga_driver.MainActivity
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.utils.OrderStatus
import uz.gita.saiga_driver.utils.extensions.fromJsonData
import javax.inject.Inject

// Created by Jamshid Isoqov on 2/18/2023
@AndroidEntryPoint
class OrderBroadcast @Inject constructor() : BroadcastReceiver() {

    @Inject
    lateinit var gson: Gson

    override fun onReceive(p0: Context?, p1: Intent?) {
        when (p1?.action) {
            OrderStatus.ACCEPT.name -> {
                val orderString = p1.getStringExtra("order")
                if (orderString != null) {
                    val order = gson.fromJsonData<OrderResponse>(orderString,
                        object : TypeToken<OrderResponse>() {}.type)
                    MainActivity.activity.showAlerter(order)
                }
            }
        }
    }
}