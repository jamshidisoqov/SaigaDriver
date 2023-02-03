package uz.gita.saiga_driver.data.remote.request.order

data class DirectionRequest(
    val addressFrom: AddressRequest,
    val addressTo: AddressRequest
)