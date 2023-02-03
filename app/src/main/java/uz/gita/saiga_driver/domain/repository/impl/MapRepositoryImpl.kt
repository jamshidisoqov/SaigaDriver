package uz.gita.saiga_driver.domain.repository.impl

import android.annotation.SuppressLint
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.utils.ResultData
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val geocoder: Geocoder,
    private val fusedLocationClient: FusedLocationProviderClient
) : MapRepository {

    override fun getAddressByLocation(latLng: LatLng): Flow<ResultData<String>> =
        flow<ResultData<String>> {
            if (hasConnection()) {
                val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (address != null) {
                    emit(ResultData.Success(address[0].getAddressLine(0)))
                } else {
                    emit(ResultData.Success("е указан"))
                }
            } else {
                emit(ResultData.Message("No internet connection"))
            }
        }.flowOn(Dispatchers.IO)

    @SuppressLint("MissingPermission")
    override fun requestCurrentLocation(): Flow<ResultData<LatLng>> =
        callbackFlow<ResultData<LatLng>> {
            if (hasConnection()) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        trySend(ResultData.Success(LatLng(location.latitude, location.longitude)))
                    }.addOnFailureListener {
                        trySend(ResultData.Error(it))
                    }
            } else {
                trySend(ResultData.Message("No internet connection"))
            }
            awaitClose { }
        }.flowOn(Dispatchers.IO)
}