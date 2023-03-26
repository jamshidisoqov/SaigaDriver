package uz.gita.saiga_driver.directions

// Created by Jamshid Isoqov on 12/14/2022
interface LoginScreenDirection {

    suspend fun navigateToVerifyScreen(phone: String)

    suspend fun navigateToRegisterScreen()

    suspend fun navigateToPermissionCheck()

}