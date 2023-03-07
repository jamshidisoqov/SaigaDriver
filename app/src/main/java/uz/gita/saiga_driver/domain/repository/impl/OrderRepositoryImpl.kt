package uz.gita.saiga_driver.domain.repository.impl

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import uz.gita.saiga_driver.MainActivity
import uz.gita.saiga_driver.data.remote.api.OrderApi
import uz.gita.saiga_driver.data.remote.request.order.AddressRequest
import uz.gita.saiga_driver.data.remote.request.order.DirectionRequest
import uz.gita.saiga_driver.data.remote.request.order.EndOrderRequest
import uz.gita.saiga_driver.data.remote.request.order.OrderRequest
import uz.gita.saiga_driver.data.remote.response.BaseResponse
import uz.gita.saiga_driver.data.remote.response.order.ActiveOrderResponse
import uz.gita.saiga_driver.data.remote.response.order.OrderResponse
import uz.gita.saiga_driver.domain.entity.ReceivedOrderResponse
import uz.gita.saiga_driver.domain.entity.TripDate
import uz.gita.saiga_driver.domain.entity.TripWithDate
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.utils.ResultData
import uz.gita.saiga_driver.utils.extensions.fromJsonData
import uz.gita.saiga_driver.utils.extensions.func
import uz.gita.saiga_driver.utils.extensions.getBackendTimeFormat
import uz.gita.saiga_driver.utils.extensions.log
import javax.inject.Inject

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi,
    private val gson: Gson,
    private val stompClient: StompClient
) : OrderRepository {

    override var ordersLiveData: MutableLiveData<ResultData<List<OrderResponse>>> =
        MutableLiveData()

    private var orders = arrayListOf<OrderResponse>()

    private val type by lazy { object : TypeToken<BaseResponse<ActiveOrderResponse>>() {}.type }

    private var compositeDisposable: CompositeDisposable? = null


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

    @SuppressLint("CheckResult")
    override fun getAllOrders() {
        try {
            stompClient.topic("/topic/new-order-from-user")
                .doOnError {
                    ordersLiveData.postValue(ResultData.Error(it))
                }
                .subscribe {
                    val order = fromGsonData(it.payload)
                    MainActivity.activity.createNotification(order)
                    orders.add(order)
                    ordersLiveData.postValue(ResultData.Success(orders))
                }
        } catch (e: Exception) {
            ordersLiveData.value = ResultData.Error(e)
        }
    }

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
                    val list = it.body.data
                    val newTripList = arrayListOf<TripWithDate>()
                    val entry = list.groupBy { order ->
                        order.timeWhen?.getBackendTimeFormat()
                    }
                    entry.entries.forEach { entries ->
                        newTripList.add(TripDate(entries.key!!))
                        newTripList.addAll(entries.value)
                    }
                    emit(ResultData.Success(newTripList))
                }.onMessage {
                    emit(ResultData.Message(it))
                }.onError {
                    emit(ResultData.Error(it))
                }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    override suspend fun getAllActiveOrders() {
        orderApi.getAllUserOrders().func(gson).onSuccess {
            orders.clear()
            orders.addAll(it.body.data)
            ordersLiveData.postValue(ResultData.Success(orders))
        }.onMessage {
            ordersLiveData.value = ResultData.Message(it)
        }.onError {
            ordersLiveData.value = ResultData.Error(it)
        }
    }

    @SuppressLint("CheckResult")
    private fun receivedOrders() {
        try {
            stompClient.topic("/topic/received-order-from-user")
                .doOnError {
                    ordersLiveData.value = ResultData.Error(it)
                }
                .subscribe {
                    log(it.payload)
                    val order = gson.fromJsonData<BaseResponse<ReceivedOrderResponse>>(
                        it.payload,
                        object : TypeToken<BaseResponse<ReceivedOrderResponse>>() {}.type
                    )
                    orders.removeIf { response ->
                        order.body.order.id == response.id
                    }
                    ordersLiveData.postValue(ResultData.Success(orders))
                }
        } catch (e: Exception) {
            ordersLiveData.value = ResultData.Error(e)
        }
    }

    @SuppressLint("CheckResult")
    private fun cancelOrders() {
        try {
            stompClient.topic("/topic/cancel-order-from-user")
                .doOnError {
                    ordersLiveData.value = ResultData.Error(it)
                }
                .subscribe {
                    log("DELETED")
                    val order = gson.fromJsonData<BaseResponse<ReceivedOrderResponse>>(
                        it.payload,
                        object : TypeToken<BaseResponse<ReceivedOrderResponse>>() {}.type
                    )
                    orders.removeIf { response ->
                        order.body.order.id == response.id
                    }
                    ordersLiveData.postValue(ResultData.Success(orders))
                }
        } catch (e: Exception) {
            ordersLiveData.value = ResultData.Error(e)
        }
    }

    override fun socketConnect() {
        try {
            resetSubscriptions()
            stompClient.withClientHeartbeat(2000).withServerHeartbeat(2000)
            resetSubscriptions()
            val disLifecycle = stompClient.lifecycle()
                .doOnError {
                    ordersLiveData.value = (ResultData.Error(it))
                }
                .subscribeOn(Schedulers.io())
                .doOnError {
                    ordersLiveData.value = (ResultData.Error(it))
                }
                .subscribe { lifecycleEvent: LifecycleEvent ->
                    when (lifecycleEvent.type) {
                        LifecycleEvent.Type.OPENED -> {
                            receivedOrders()
                            getAllOrders()
                            cancelOrders()
                        }
                        LifecycleEvent.Type.ERROR -> {
                            log("Error")
                        }
                        LifecycleEvent.Type.CLOSED -> {
                            resetSubscriptions()
                        }
                        LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                            log("FAILED")
                        }
                    }
                }

            compositeDisposable?.add(disLifecycle)

            stompClient.connect()
        } catch (_: Exception) {

        }
    }

    override fun socketDisconnect() {
        stompClient.disconnect()
        compositeDisposable?.dispose()
    }

    override fun endOrder(endOrderRequest: EndOrderRequest): Flow<ResultData<Any>> = flow {
        emit(orderApi.endOrder(endOrderRequest = endOrderRequest).func(gson))
    }.catch { error ->
        emit(ResultData.Error(error))
    }.flowOn(Dispatchers.IO)

    override fun cancelOrder(id: Long): Flow<ResultData<Any>> = flow {
        emit(orderApi.cancelOrder(id).func(gson))
    }.catch { error ->
        emit(ResultData.Error(error))
    }.flowOn(Dispatchers.IO)

    private fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable?.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }

    private fun fromGsonData(message: String): OrderResponse {
        val baseOrderResponse = gson.fromJson<BaseResponse<ActiveOrderResponse>>(message, type)
        return baseOrderResponse.body.order
    }

}