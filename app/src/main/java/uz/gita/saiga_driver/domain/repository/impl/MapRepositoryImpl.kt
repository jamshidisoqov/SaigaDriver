package uz.gita.saiga_driver.domain.repository.impl

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import uz.gita.saiga_driver.data.remote.api.NominationApi
import uz.gita.saiga_driver.data.remote.response.nomination.NominationResponse
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.utils.ResultData
import uz.gita.saiga_driver.utils.extensions.func
import uz.gita.saiga_driver.utils.hasConnection
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val nominationApi: NominationApi,
    private val gson: Gson,
    private val fusedLocationClient: FusedLocationProviderClient
) : MapRepository {

    override fun getAddressByLocation(latLng: LatLng) =
        flow<ResultData<String>> {
            if (hasConnection()) {
                nominationApi.getNominationAddress(lat = latLng.latitude, lon = latLng.longitude)
                    .func(gson = gson)
                    .onSuccess {
                        emit(ResultData.Success(it.features[0].properties.display_name ?: ""))
                    }.onMessage {
                        emit(ResultData.Success("Undefined area"))
                    }.onError {
                        emit(ResultData.Success("Undefined area"))
                    }
            } else {
                emit(ResultData.Message("No internet connection"))
            }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    @SuppressLint("MissingPermission")
    override fun requestCurrentLocation(): Flow<ResultData<LatLng>> =
        callbackFlow<ResultData<LatLng>> {
            if (hasConnection()) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        location?.let {
                            trySend(
                                ResultData.Success(
                                    LatLng(
                                        location.latitude,
                                        location.longitude
                                    )
                                )
                            )
                        }
                    }.addOnFailureListener {
                        trySend(ResultData.Error(it))
                    }
            } else {
                trySend(ResultData.Message("No internet connection"))
            }
            awaitClose()
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)

    override fun getAddressProperties(latLng: LatLng): Flow<ResultData<NominationResponse>> =
        flow<ResultData<NominationResponse>> {
            if (hasConnection()) {
                nominationApi.getNominationAddress(lat = latLng.latitude, lon = latLng.longitude)
                    .func(gson = gson)
                    .onSuccess {
                        emit(ResultData.Success(it))
                    }.onMessage {
                        emit(ResultData.Message("Undefined area"))
                    }.onError {
                        emit(ResultData.Error(it))
                    }
            } else {
                emit(ResultData.Message("No internet connection"))
            }
        }.catch { error ->
            emit(ResultData.Error(error))
        }.flowOn(Dispatchers.IO)
}