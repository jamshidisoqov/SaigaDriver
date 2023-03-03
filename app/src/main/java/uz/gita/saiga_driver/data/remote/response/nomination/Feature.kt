package uz.gita.saiga_driver.data.remote.response.nomination

data class Feature(
    val bbox: List<Double>,
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)