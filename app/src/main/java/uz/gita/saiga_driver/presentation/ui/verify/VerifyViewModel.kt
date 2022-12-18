package uz.gita.saiga_driver.presentation.ui.verify

import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/15/2022
interface VerifyViewModel : BaseViewModel {

    fun resendCode()

    fun verifyCode(code: String)

}