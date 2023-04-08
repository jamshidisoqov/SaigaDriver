package uz.gita.saiga_driver.app.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.domain.repository.MapRepository

@HiltWorker
class LocationWork @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val mapRepository: MapRepository,
    private val mySharedPref: MySharedPref
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        var res: Result = Result.success()
        try {
            val result = mapRepository.requestCurrentLocation().first()
            result.onSuccess {
                mySharedPref.lat = it.latitude.toString()
                mySharedPref.lon = it.longitude.toString()
                res = Result.success()
            }.onError {
                res = Result.failure()
                return@onError
            }.onMessage {
                res = Result.retry()
            }
        } catch (e: Exception) {
            res = Result.retry()
        }
        return res
    }
}