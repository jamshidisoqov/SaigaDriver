package uz.gita.saiga_driver.app.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.asDeferred
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.utils.extensions.log

@HiltWorker
class MinPriceWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val mySharedPref: MySharedPref
) : CoroutineWorker(context, workerParameters) {

    private val firebaseRemoteConfig: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    override suspend fun doWork(): Result {
        log("Worker")
        val default = mapOf("min_price" to 8000)
        firebaseRemoteConfig.setDefaultsAsync(default)
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val minPrice = firebaseRemoteConfig.getDouble("min_price")
                mySharedPref.minPrice = minPrice.toString()
            }
        }.asDeferred().await()
        return Result.success()
    }
}