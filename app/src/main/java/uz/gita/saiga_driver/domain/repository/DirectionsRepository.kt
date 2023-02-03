package uz.gita.saiga_driver.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.saiga_driver.data.remote.response.order.DirectionResponse
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.utils.ResultData

// Created by Jamshid Isoqov on 12/14/2022
interface DirectionsRepository {

    fun getAllDirectionalTaxis(): Flow<ResultData<List<OrderResponse>>>

    fun getAllFavouritesAddress():Flow<ResultData<List<DirectionResponse>>>

}