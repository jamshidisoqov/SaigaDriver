package uz.gita.saiga_driver.data.remote.request.order

data class AddressRequest(
    val lat: Double? = null,
    val lon: Double? = null,
    val title: String? = null
)