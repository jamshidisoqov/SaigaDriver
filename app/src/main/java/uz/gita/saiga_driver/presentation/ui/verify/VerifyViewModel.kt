package uz.gita.saiga_driver.presentation.ui.verify

import kotlinx.coroutines.flow.SharedFlow
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/15/2022
interface VerifyViewModel : BaseViewModel {

    val openPermissionChecker:SharedFlow<Unit>

    fun resendCode()

    fun verifyCode(code: String)

}