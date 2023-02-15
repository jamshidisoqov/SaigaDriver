package uz.gita.saiga_driver.data.remote.request.order

import com.google.gson.annotations.SerializedName

data class AddressRequest(
    @SerializedName("latitude")
    val lat: Double? = null,
    @SerializedName("longitude")
    val lon: Double? = null,
    val title: String? = null
)