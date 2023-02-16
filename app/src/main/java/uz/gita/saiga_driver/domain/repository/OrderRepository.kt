package uz.gita.saiga_driver.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.entity.TripWithDate
import uz.gita.saiga_driver.utils.ResultData

// Created by Jamshid Isoqov on 12/14/2022
interface OrderRepository {

    var ordersLiveData:MutableLiveData<ResultData<List<OrderResponse>>>

    fun order(
        whereFrom: String,
        whereFromLatLng: LatLng,
        whereTo: String? = null,
        whereToLatLng: LatLng? = null,
        price: Double,
        schedule: String?,
        comment: String?
    ): Flow<ResultData<OrderResponse>>


    suspend fun getAllOrders()

    fun receiveOrder(orderId:Long):Flow<ResultData<OrderResponse>>

    fun getAllHistory():Flow<ResultData<List<TripWithDate>>>

    suspend fun getAllActiveOrders()

     fun socketConnect()

    suspend fun socketDisconnect()

}