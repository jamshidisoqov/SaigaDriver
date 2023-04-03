package uz.gita.saiga_driver.data.remote.response.order

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize
import uz.gita.saiga_driver.data.remote.response.auth.UserResponse
import uz.gita.saiga_driver.domain.entity.TripWithDate

// Created by Jamshid Isoqov on 1/31/2023
@Parcelize
data class OrderResponse(
    val id: Long,
    val fromUser: UserResponse,
    val money: String,
    val direction: DirectionResponse,
    val toUser: UserResponse? = null,
    val comment: String? = null,
    val timeWhen: String? = null,
    @Expose
    val distance:String = "0"
):TripWithDate,Parcelable
