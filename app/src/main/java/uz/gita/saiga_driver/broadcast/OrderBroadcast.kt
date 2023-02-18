package uz.gita.saiga_driver.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import uz.gita.saiga_driver.utils.OrderStatus
import uz.gita.saiga_driver.utils.extensions.log

// Created by Jamshid Isoqov on 2/18/2023
class OrderBroadcast : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        when(p1?.action){
            OrderStatus.ACCEPT.name->{
                log("Keldi")
            }
            OrderStatus.BACK.name->{

            }
        }
    }
}