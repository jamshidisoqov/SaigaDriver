package uz.gita.saiga_driver.domain.repository.orders

import kotlinx.coroutines.flow.Flow
import uz.gita.saiga_driver.data.remote.response.DirectionalTaxiData
import uz.gita.saiga_driver.data.remote.response.OrderData
import uz.gita.saiga_driver.utils.ResultData

// Created by Jamshid Isoqov on 12/19/2022
interface OrderRepository {

    fun getBalanceFlow(): Flow<ResultData<Double>>

    fun getOrders(): Flow<ResultData<Int>>

    fun getIncomeFlow(): Flow<ResultData<Double>>

    fun getExpanseFlow(): Flow<ResultData<Double>>

    fun getAllMyDirections(): Flow<ResultData<List<DirectionalTaxiData>>>

    fun getAllOrders(): Flow<ResultData<List<OrderData>>>

}