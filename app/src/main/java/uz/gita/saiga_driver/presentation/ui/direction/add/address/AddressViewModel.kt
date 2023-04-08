package uz.gita.saiga_driver.presentation.ui.direction.add.address

import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.data.remote.response.order.AddressResponse

// Created by Jamshid Isoqov on 2/24/2023
interface AddressViewModel {

    val allAddressFlow: StateFlow<List<AddressResponse>>

    fun searchAddress(query: String)

}