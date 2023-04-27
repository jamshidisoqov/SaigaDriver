package uz.gita.saiga_driver.data.remote.response.auth

import com.google.gson.annotations.SerializedName

data class UserBaseResponse(
    @SerializedName(value = "user")
    val userResponse: UserResponse,
    @SerializedName(value = "token")
    val token:String
)