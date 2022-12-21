package uz.gita.saiga_driver.domain.repository.directions

import kotlinx.coroutines.flow.Flow
import uz.gita.saiga_driver.data.remote.request.DirectionalTaxiDto
import uz.gita.saiga_driver.data.remote.response.AddressData
import uz.gita.saiga_driver.data.remote.response.DirectionalTaxiData
import uz.gita.saiga_driver.utils.ResultData

// Created by Jamshid Isoqov on 12/19/2022
interface DirectionsRepository {

    fun getAllDirection(): Flow<ResultData<List<DirectionalTaxiData>>>

    fun deleteDirection(directionalTaxiData: DirectionalTaxiData)

    fun updateDirection(directionalTaxiData: DirectionalTaxiData)

    fun addDirection(directionalTaxiDto: DirectionalTaxiDto):Flow<ResultData<DirectionalTaxiData>>

    fun getAllExistsAddress():Flow<ResultData<List<AddressData>>>

}