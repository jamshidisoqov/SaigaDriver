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
class FirebaseSyncWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val mySharedPref: MySharedPref
) : CoroutineWorker(context, workerParameters) {

    private val firebaseRemoteConfig: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    override suspend fun doWork(): Result {
        log("Worker")
        mySharedPref.apply {
            val default = mapOf(
                MIN_PRICE to minPrice,
                BASE_URL to baseUrl,
                IS_APP_INTRO to appIsIntro,
                MIN_WAY_CALCULATION to minWayCalculation,
                PRICE_TO_PER_KM to pricePerKm,
                SOCKET_BASE_URL to socketBaseUrl,
                WAITING_FIRST_TIME_PRICE to waitingFirstTimePrice,
                WAITING_PRICE_PER_MIN to waitingPricePerMin,
                WAITING_TIME_FIRST to waitingTimeFirst
            )
            firebaseRemoteConfig.setDefaultsAsync(default)
            firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    val minPrice = firebaseRemoteConfig.getDouble(MIN_PRICE)
                    val baseUrl = firebaseRemoteConfig.getString(BASE_URL)
                    val isAppIntro = firebaseRemoteConfig.getBoolean(IS_APP_INTRO)
                    val minWayCalculation = firebaseRemoteConfig.getDouble(MIN_WAY_CALCULATION)
                    val priceToPerKm = firebaseRemoteConfig.getDouble(PRICE_TO_PER_KM)
                    val socketBaseUrl = firebaseRemoteConfig.getString(SOCKET_BASE_URL)
                    val waitingFirstTimePrice =
                        firebaseRemoteConfig.getDouble(WAITING_FIRST_TIME_PRICE)
                    val waitingPricePerMin = firebaseRemoteConfig.getDouble(WAITING_PRICE_PER_MIN)
                    val waitingTimeFirst = firebaseRemoteConfig.getLong(WAITING_TIME_FIRST)
                    mySharedPref.minPrice = minPrice.toString()
                    mySharedPref.baseUrl = baseUrl
                    mySharedPref.appIsIntro = isAppIntro
                    mySharedPref.minWayCalculation = minWayCalculation.toString()
                    mySharedPref.pricePerKm = priceToPerKm.toString()
                    mySharedPref.socketBaseUrl = socketBaseUrl
                    mySharedPref.waitingFirstTimePrice = waitingFirstTimePrice.toString()
                    mySharedPref.waitingPricePerMin = waitingPricePerMin.toString()
                    mySharedPref.waitingTimeFirst = waitingTimeFirst
                }
            }.asDeferred().await()
        }
        return Result.success()
    }

    companion object {
        const val MIN_PRICE = "min_price"
        const val BASE_URL = "base_url"
        const val IS_APP_INTRO = "is_app_intro"
        const val MIN_WAY_CALCULATION = "min_way_calculation"
        const val PRICE_TO_PER_KM = "price_to_per_km"
        const val SOCKET_BASE_URL = "socket_base_url"
        const val WAITING_FIRST_TIME_PRICE = "waiting_first_time_price"
        const val WAITING_PRICE_PER_MIN = "waiting_price_per_min"
        const val WAITING_TIME_FIRST = "waiting_time_first"
    }
}
