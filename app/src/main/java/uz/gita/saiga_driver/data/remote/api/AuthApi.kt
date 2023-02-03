package uz.gita.saiga_driver.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.request.auth.LoginRequest
import uz.gita.saiga_driver.data.remote.request.auth.UpdateUserRequest
import uz.gita.saiga_driver.data.remote.request.auth.UserRequest
import uz.gita.saiga_driver.data.remote.response.auth.AuthResponse

// Created by Jamshid Isoqov on 12/14/2022
interface AuthApi {

    @PUT("/api/users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Long,
        @Body updateUserRequest: UpdateUserRequest
    ): Response<BaseResponse<AuthResponse>>

    @POST("/api/users/sign-up")
    suspend fun registerUser(
        @Body userRequest: UserRequest
    ): Response<BaseResponse<AuthResponse>>

    @POST("/api/users/sign-in")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<BaseResponse<AuthResponse>>

}