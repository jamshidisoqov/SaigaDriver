package uz.gita.saiga_driver.directions

// Created by Jamshid Isoqov on 12/18/2022
interface RegisterScreenDirection {

    suspend fun navigateToVerify(phone: String)

    suspend fun navigateToLogin()

    suspend fun navigateToPermissionCheck()

}