package uz.gita.saiga_driver.data.remote.response.nomination

data class NominationResponse(
    val features: List<Feature>,
    val licence: String,
    val type: String
)