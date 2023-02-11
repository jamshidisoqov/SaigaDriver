package uz.gita.saiga_driver.domain.repository

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.entity.TripWithDate
import uz.gita.saiga_driver.utils.ResultData

// Created by Jamshid Isoqov on 12/14/2022
interface OrderRepository {

    fun order(
        whereFrom: String,
        whereFromLatLng: LatLng,
        whereTo: String? = null,
        whereToLatLng: LatLng? = null,
        price: Double,
        schedule: String?,
        comment: String?
    ): Flow<ResultData<OrderResponse>>


    fun getAllOrders(): Flow<ResultData<List<OrderResponse>>>

    fun receiveOrder(orderId:Long):Flow<ResultData<OrderResponse>>

    fun getAllHistory():Flow<ResultData<List<TripWithDate>>>

    suspend fun socketConnect()

    suspend fun socketDisconnect()

}