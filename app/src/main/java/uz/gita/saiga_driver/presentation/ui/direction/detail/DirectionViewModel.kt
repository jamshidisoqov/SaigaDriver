package uz.gita.saiga_driver.presentation.ui.direction.detail

import kotlinx.coroutines.flow.SharedFlow
import uz.gita.saiga_driver.data.remote.response.map.RouteData
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/21/2022
interface DirectionViewModel : BaseViewModel {

    val routes: SharedFlow<RouteData>

    fun delete(directionalTaxiData: DirectionalTaxiData)

    fun findRoutes(directionalTaxiData: DirectionalTaxiData)

}