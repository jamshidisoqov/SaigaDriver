package uz.gita.saiga_driver.domain.repository

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import uz.gita.saiga_driver.utils.ResultData

// Created by Jamshid Isoqov on 12/16/2022
interface MapRepository {

    fun getAddressByLocation(latLng: LatLng): Flow<ResultData<String>>

    fun requestCurrentLocation(): Flow<ResultData<LatLng>>

}