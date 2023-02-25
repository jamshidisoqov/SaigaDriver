package uz.gita.saiga_driver.data.remote.response.nomination

data class Geometry(
    val coordinates: List<Double> = emptyList(),
    val type: String?
)