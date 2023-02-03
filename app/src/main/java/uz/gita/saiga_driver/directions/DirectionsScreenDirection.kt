package uz.gita.saiga_driver.directions

// Created by Jamshid Isoqov on 12/19/2022
interface DirectionsScreenDirection {

    suspend fun navigateToAddDirection()

    suspend fun navigateToDirectionDetail(directionalTaxiData: DirectionalTaxiData)

}