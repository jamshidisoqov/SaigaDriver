package uz.gita.saiga_driver.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uz.gita.saiga_driver.data.remote.request.order.OrderRequest
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.response.DataResponse
import uz.gita.saiga_driver.data.remote.response.order.BaseOrderResponse
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse

// Created by Jamshid Isoqov on 12/14/2022
interface OrderApi {

    @POST("/api/orders/driver-order")
    suspend fun orderByTaxi(@Body orderRequest: OrderRequest): Response<BaseResponse<BaseOrderResponse>>

    @POST("/api/orders/receive/{orderId}")
    suspend fun receiveOrder(@Path("orderId") orderId: Long): Response<BaseResponse<BaseOrderResponse>>

    @GET("/api/orders/non-received/user")
    suspend fun getAllUserOrders(): Response<BaseResponse<DataResponse<List<OrderResponse>>>>

    @GET("/api/orders/history")
    suspend fun getAllHistory(): Response<BaseResponse<DataResponse<List<OrderResponse>>>>




}