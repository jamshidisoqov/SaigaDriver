package uz.gita.saiga_driver.data.remote.response.nomination

data class Address(
    val city: String,
    val country: String,
    val country_code: String,
    val county: String,
    val house_number: String,
    val postcode: String,
    val road: String,
    val state: String,
    val suburb: String
)