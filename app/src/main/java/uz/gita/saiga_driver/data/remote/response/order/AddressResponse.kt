package uz.gita.saiga_driver.data.remote.response.order

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Created by Jamshid Isoqov on 1/31/2023
@Parcelize
data class AddressResponse(
    val id: Long,
    @SerializedName("latitude")
    val lat: Double? = null,
    @SerializedName("longitude")
    val lon: Double? = null,
    val title: String? = null,
    @Expose
    val distance: String? = null
) : Parcelable