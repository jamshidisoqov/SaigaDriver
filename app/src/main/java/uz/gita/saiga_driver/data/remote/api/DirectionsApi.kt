package uz.gita.saiga_driver.data.remote.api

import retrofit2.Response
import retrofit2.http.*
import uz.gita.saiga_driver.data.remote.request.direction.StaticAddressRequest
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.response.DataResponse
import uz.gita.saiga_driver.data.remote.response.StaticAddressResponse
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse

// Created by Jamshid Isoqov on 12/14/2022
interface DirectionsApi {

    @GET("/api/orders/non-received/driver")
    suspend fun getAllDirections(): Response<BaseResponse<DataResponse<List<OrderResponse>>>>

    @POST("/admin/static/address")
    suspend fun addStaticAddress(@Body staticAddressRequest: StaticAddressRequest):Response<BaseResponse<StaticAddressResponse>>

    @GET("/api/address/static-address")
    suspend fun getAllStaticAddress():Response<BaseResponse<DataResponse<List<StaticAddressResponse>>>>

    @PUT("/api/orders/cancel-non-received-order/{id}")
    suspend fun cancelOrder(@Path("id") id: Long):Response<Any>

}