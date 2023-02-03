package uz.gita.saiga_driver.presentation.ui.direction.add

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.data.remote.response.order.DirectionResponse
import uz.gita.saiga_driver.utils.BaseViewModel
import java.sql.Timestamp

// Created by Jamshid Isoqov on 12/20/2022
interface AddDirectionViewModel : BaseViewModel {

    val backFlow: SharedFlow<Unit>

    val allDirections: StateFlow<List<DirectionResponse>>

    fun addDirection(whereFrom: String, whereTo: String, schedule: Timestamp, price: Double)

}