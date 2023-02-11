package uz.gita.saiga_driver.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.response.DataResponse
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse

// Created by Jamshid Isoqov on 12/14/2022
interface DirectionsApi {

    @GET("/api/orders/non-received/driver")
    suspend fun getAllDirections(): Response<BaseResponse<DataResponse<List<OrderResponse>>>>

}