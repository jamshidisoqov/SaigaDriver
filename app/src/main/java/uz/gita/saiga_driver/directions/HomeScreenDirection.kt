package uz.gita.saiga_driver.directions

// Created by Jamshid Isoqov on 12/19/2022
interface HomeScreenDirection {

    suspend fun navigateToOrders()

    suspend fun navigateToNotification()

    suspend fun navigateToFinance()

    suspend fun navigateToDirectionDetails(directionalTaxiData: DirectionalTaxiData)


}