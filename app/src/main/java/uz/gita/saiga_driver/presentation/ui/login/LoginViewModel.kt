package uz.gita.saiga_driver.presentation.ui.login

import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/18/2022
interface LoginViewModel:BaseViewModel {
    fun navigateToRegister()

    fun login(phone: String)
}