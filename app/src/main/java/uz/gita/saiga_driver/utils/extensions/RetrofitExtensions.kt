package uz.gita.saiga_driver.utils.extensions

import com.google.gson.Gson
import retrofit2.Response
import uz.gita.saiga_driver.utils.MessageData
import uz.gita.saiga_driver.utils.ResultData

// Created by Jamshid Isoqov on 12/25/2022

fun <T> Response<T>.func(gson: Gson): ResultData<T> {
    try {
        val data = this
        if (data.isSuccessful) {
            return if (data.body() != null) {
                val body = data.body()!!
                ResultData.Success(body)
            } else {
                ResultData.Error(Throwable("Body null"))
            }
        }
        if (this.code() == 403) {
            return ResultData.Message("Token expired")
        }
        return when(this.code()){
            in (300..399)->{
                ResultData.Error(Throwable(message = "Есть проблемы с интернетом"))
            }
            in (400..499)->{
                val messageData = gson.fromJson(errorBody()!!.string(), MessageData::class.java)
                ResultData.Error(Throwable(message = messageData.message))
            }
            in (500..599)->{
                ResultData.Error(Throwable(message = "Есть проблемы с сервером"))
            }
            else->{
                ResultData.Error(Throwable(message = "Произошла неизвестная ошибка"))
            }
        }

    } catch (e: Exception) {
        return ResultData.Error(e)
    }
}