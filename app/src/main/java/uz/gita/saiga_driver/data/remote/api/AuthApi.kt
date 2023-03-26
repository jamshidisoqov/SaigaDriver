package uz.gita.saiga_driver.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import uz.gita.saiga_driver.data.remote.request.auth.BalanceRequest
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.request.auth.LoginRequest
import uz.gita.saiga_driver.data.remote.request.auth.UpdateUserRequest
import uz.gita.saiga_driver.data.remote.request.auth.UserRequest
import uz.gita.saiga_driver.data.remote.response.DataResponse
import uz.gita.saiga_driver.data.remote.response.DriverBalanceResponse
import uz.gita.saiga_driver.data.remote.response.auth.AuthResponse
import uz.gita.saiga_driver.data.remote.response.auth.BalanceBaseResponse
import uz.gita.saiga_driver.data.remote.response.auth.CabinetBaseResponse
import uz.gita.saiga_driver.data.remote.response.auth.CabinetResponse

// Created by Jamshid Isoqov on 12/14/2022
interface AuthApi {

    @PUT("/api/auth/{id}")
    suspend fun updateUser(
        @Path("id") userId: Long,
        @Body updateUserRequest: UpdateUserRequest
    ): Response<BaseResponse<AuthResponse>>

    @POST("/api/auth/sign-up")
    suspend fun registerUser(
        @Body userRequest: UserRequest
    ): Response<BaseResponse<CabinetBaseResponse>>

    @POST("/api/auth/sign-in")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<BaseResponse<AuthResponse>>

    @POST("/api/admin/top-up-balance")
    suspend fun topUpBalance(@Body balanceRequest: BalanceRequest):Response<BaseResponse<BalanceBaseResponse>>


    @GET("api/driver/balance-in-out")
    suspend fun getUserBalance():Response<BaseResponse<DataResponse<DriverBalanceResponse>>>

}