package uz.gita.saiga_driver.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import uz.gita.saiga_driver.data.remote.request.order.OrderRequest
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.response.order.BaseOrderResponse

// Created by Jamshid Isoqov on 12/14/2022
interface OrderApi {

    @POST("orders/user-order")
    suspend fun orderByTaxi(@Body orderRequest: OrderRequest): Response<BaseResponse<BaseOrderResponse>>
}