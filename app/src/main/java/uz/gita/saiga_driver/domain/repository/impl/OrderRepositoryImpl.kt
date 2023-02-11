package uz.gita.saiga_driver.domain.repository.impl

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.saiga_driver.data.remote.api.OrderApi
import uz.gita.saiga_driver.data.remote.request.order.AddressRequest
import uz.gita.saiga_driver.data.remote.request.order.DirectionRequest
import uz.gita.saiga_driver.data.remote.request.order.OrderRequest
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.entity.TripWithDate
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.utils.ResultData
import uz.gita.saiga_driver.utils.extensions.func
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi,
    private val gson: Gson
) : OrderRepository {


    override fun order(
        whereFrom: String,
        whereFromLatLng: LatLng,
        whereTo: String?,
        whereToLatLng: LatLng?,
        price: Double,
        schedule: String?,
        comment: String?
    ): Flow<ResultData<OrderResponse>> = flow<ResultData<OrderResponse>> {
        orderApi.orderByTaxi(
            OrderRequest(
                DirectionRequest(
                    addressFrom = AddressRequest(
                        whereFromLatLng.latitude, whereFromLatLng.longitude, whereFrom
                    ),
                    addressTo = AddressRequest(
                        whereToLatLng?.latitude, whereToLatLng?.longitude, whereTo
                    ),
                ),
                amountOfMoney = price.toString(),
                comment = comment,
                timeWhen = schedule ?: ""
            )
        ).func(gson = gson).onSuccess {
            emit(ResultData.Success(it.body.data))
        }.onMessage {
            emit(ResultData.Message(it))
        }.onError {
            emit(ResultData.Error(it))
        }
    }.catch { error ->
        emit(ResultData.Error(error))
    }.flowOn(Dispatchers.IO)


    override fun getAllOrders(): Flow<ResultData<List<OrderResponse>>> =
        flow<ResultData<List<OrderResponse>>> {
            orderApi.getAllUserOrders().func(gson).onSuccess {
                emit(ResultData.Success(it.body.data))
            }.onMessage {
                emit(ResultData.Message(it))
            }.onError {
                emit(ResultData.Error(it))
            }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    override fun receiveOrder(orderId: Long): Flow<ResultData<OrderResponse>> =
        flow<ResultData<OrderResponse>> {
            orderApi.receiveOrder(orderId).func(gson)
                .onSuccess {
                    emit(ResultData.Success(it.body.data))
                }.onMessage {
                    emit(ResultData.Message(it))
                }.onError {
                    emit(ResultData.Error(it))
                }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    override fun getAllHistory(): Flow<ResultData<List<TripWithDate>>> =
        flow<ResultData<List<TripWithDate>>> {
            orderApi.getAllHistory().func(gson)
                .onSuccess {
                    emit(ResultData.Success(it.body))
                }.onMessage {
                    emit(ResultData.Message(it))
                }.onError {
                    emit(ResultData.Error(it))
                }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    override suspend fun socketConnect() {
        TODO("Not yet implemented")
    }

    override suspend fun socketDisconnect() {
        TODO("Not yet implemented")
    }
}