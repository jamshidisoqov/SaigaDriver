package uz.gita.saiga_driver.directions

// Created by Jamshid Isoqov on 12/19/2022
interface ProfileScreenDirection {

    suspend fun navigateToDirections()

    suspend fun navigateToFinance()

    suspend fun navigateToSettings()

    suspend fun navigateToCustomerCare()

}