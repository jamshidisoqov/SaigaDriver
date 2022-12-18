package uz.gita.saiga_driver.presentation.ui.register

import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/15/2022
interface RegisterViewModel : BaseViewModel {

    fun register(phone: String, firstName: String, lastName: String)

    fun navigateToLogin()

}