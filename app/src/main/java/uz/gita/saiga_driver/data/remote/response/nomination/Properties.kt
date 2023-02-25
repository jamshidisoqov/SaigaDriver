package uz.gita.saiga_driver.data.remote.response.nomination

import uz.gita.saiga_driver.data.remote.response.nomination.Address

data class Properties(
    val address: Address?,
    val category: String?,
    val display_name: String?,
    val importance: Double?,
    val name: Any?,
    val osm_type: String?,
    val place_id: Int?,
    val place_rank: Int?,
    val type: String?
)