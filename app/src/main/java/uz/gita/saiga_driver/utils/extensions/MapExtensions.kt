package uz.gita.saiga_driver.utils.extensions

import com.google.android.gms.maps.model.LatLng
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
// Created by Jamshid Isoqov on 2/4/2023
fun calculationByDistance(from: LatLng, to: LatLng): Double {
    val radius = 6371
    val lat1 = from.latitude
    val lat2 = to.latitude
    val lon1 = from.longitude
    val lon2 = to.longitude
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = (sin(dLat / 2) * sin(dLat / 2)
            + (cos(Math.toRadians(lat1))
            * cos(Math.toRadians(lat2)) * sin(dLon / 2)
            * sin(dLon / 2)))
    val c = 2 * asin(sqrt(a))
    return (radius * c).getFormat(2).toDouble()
}