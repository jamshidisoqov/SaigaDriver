package uz.gita.saiga_driver.domain.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import uz.gita.saiga_driver.data.remote.request.direction.StaticAddressRequest
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.response.StaticAddressResponse
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.utils.ResultData

// Created by Jamshid Isoqov on 12/14/2022
interface DirectionsRepository {

    fun getAllMyDirections(): Flow<ResultData<List<OrderResponse>>>

    suspend fun addStaticAddress(staticAddressRequest: StaticAddressRequest): Response<BaseResponse<StaticAddressResponse>>

    fun getAllStaticAddress(): Flow<ResultData<List<StaticAddressResponse>>>

}