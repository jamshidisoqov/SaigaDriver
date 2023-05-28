package uz.gita.saiga_driver.data.remote.request.auth

import com.google.gson.annotations.SerializedName

data class SmsRequest(
    @SerializedName("code")
    val smsCode: String,
    @SerializedName("phoneNumber")
    val phone: String
)
