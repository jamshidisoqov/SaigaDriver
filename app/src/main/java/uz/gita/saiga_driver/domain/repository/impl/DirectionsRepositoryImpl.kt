package uz.gita.saiga_driver.domain.repository.impl

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.data.remote.api.DirectionsApi
import uz.gita.saiga_driver.data.remote.request.direction.StaticAddressRequest
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.response.StaticAddressResponse
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.repository.DirectionsRepository
import uz.gita.saiga_driver.utils.ResultData
import uz.gita.saiga_driver.utils.extensions.func
import javax.inject.Inject

class DirectionsRepositoryImpl @Inject constructor(
    private val directionsApi: DirectionsApi,
    private val gson: Gson,
    private val mySharedPref: MySharedPref
) : DirectionsRepository {
    override fun getAllMyDirections(): Flow<ResultData<List<OrderResponse>>> =
        flow<ResultData<List<OrderResponse>>> {
            directionsApi.getAllDirections().func(gson = gson).onSuccess {
                val direction = it.body.data.filter { order ->
                    order.fromUser.id == mySharedPref.userId
                }.toList()
                emit(ResultData.Success(direction))
            }.onMessage {
                emit(ResultData.Message(it))
            }.onError {
                emit(ResultData.Error(it))
            }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    override suspend fun addStaticAddress(staticAddressRequest: StaticAddressRequest): Response<BaseResponse<StaticAddressResponse>> =
        directionsApi.addStaticAddress(staticAddressRequest)

    override fun getAllStaticAddress(): Flow<ResultData<List<StaticAddressResponse>>> =
        flow<ResultData<List<StaticAddressResponse>>> {
            directionsApi.getAllStaticAddress().func(gson).onSuccess {
                emit(ResultData.Success(it.body))
            }.onMessage {
                emit(ResultData.Message(it))
            }.onError {
                emit(ResultData.Error(it))
            }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)
}