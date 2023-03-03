package uz.gita.saiga_driver.presentation.ui.main.pages.profile

import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/19/2022
interface ProfileViewModel:BaseViewModel {

    val nameFlow: StateFlow<String>

    val phoneFlow: StateFlow<String>

    fun navigateToDirections()

    fun navigateToFinance()

    fun navigateToSettings()

    fun navigateToCustomerCare()

    fun navigateToProfileDetail()

    fun getData()

}