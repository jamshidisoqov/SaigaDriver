package uz.gita.saiga_driver.presentation.ui.direction

import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/19/2022
interface DirectionsViewModel : BaseViewModel {

    val allDirections: StateFlow<List<DirectionalTaxiData>>

    fun navigateToAddDirections()

    fun navigateToDirectionDetail(directionalTaxiData: DirectionalTaxiData)

    fun getAllDirection()

}