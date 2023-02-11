package uz.gita.saiga_driver.data.remote.response.map

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class DestinationData(
    val fromWhere: String,
    val toWhere: String,
    val start: LatLng,
    val end: LatLng
): Parcelable