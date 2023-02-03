package uz.gita.saiga_driver.domain.repository.impl

import kotlinx.coroutines.flow.Flow
import uz.gita.saiga_driver.data.remote.response.order.DirectionResponse
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.repository.DirectionsRepository
import uz.gita.saiga_driver.utils.ResultData
import javax.inject.Inject

class DirectionsRepositoryImpl @Inject constructor(

): DirectionsRepository {
    override fun getAllDirectionalTaxis(): Flow<ResultData<List<OrderResponse>>> {
        TODO("Not yet implemented")
    }

    override fun getAllFavouritesAddress(): Flow<ResultData<List<DirectionResponse>>> {
        TODO("Not yet implemented")
    }

}