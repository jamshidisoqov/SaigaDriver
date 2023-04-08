package uz.gita.saiga_driver.data.remote.response.nomination

import androidx.core.text.isDigitsOnly
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import uz.gita.saiga_driver.data.remote.response.order.AddressResponse
import uz.gita.saiga_driver.utils.extensions.calculationByDistance
import uz.gita.saiga_driver.utils.extensions.combine

data class SearchResponse(
    val lat: Double,
    val lon: Double,
    @SerializedName("display_name")
    val displayName: String
){
    fun toAddressResponse(currentLatLng: LatLng): AddressResponse {
        val builder = StringBuilder()
        displayName?.split(',')
            ?.forEachIndexed { index, s ->
                if (index < 3) {
                    if (!s.trim().isDigitsOnly()) {
                        builder.append(s.trim().plus("\n"))
                    }
                }
            }
        return AddressResponse(
            0,
            lat = lat,
            lon = lon,
            title = builder.toString().trim(),
            distance = try {
                calculationByDistance(currentLatLng, LatLng(lat, lon)).toString()
            } catch (e: Exception) {
                "0"
            }.combine("km")
        )
    }
}
