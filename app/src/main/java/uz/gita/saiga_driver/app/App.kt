package uz.gita.saiga_driver.app

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import uz.gita.saiga_driver.BuildConfig
import uz.gita.saiga_driver.app.work.LocationWork
import uz.gita.saiga_driver.app.work.FirebaseSyncWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// Created by Jamshid Isoqov on 12/12/2022


const val DEFAULT_WORK_INTERVAL_IN_MINUTES = 15L
const val SYNC_WORKER_NAME = "SYNC_WORKER"
const val MIN_PRICE_WORKER = "MIN_PRICE_WORKER"

@HiltAndroidApp
class App : Application(),Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        instance = this
        startWorks()
    }

    private fun startWorks() {
        val workManager = WorkManager.getInstance(applicationContext)


        val networkConstraint =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val locationWork = PeriodicWorkRequest.Builder(
            LocationWork::class.java,
            DEFAULT_WORK_INTERVAL_IN_MINUTES,
            TimeUnit.MINUTES
        )
            .setConstraints(networkConstraint)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                DEFAULT_WORK_INTERVAL_IN_MINUTES,
                TimeUnit.MINUTES
            )
            .build()

        val priceWork = PeriodicWorkRequest.Builder(
            FirebaseSyncWorker::class.java,
            DEFAULT_WORK_INTERVAL_IN_MINUTES,
            TimeUnit.MINUTES
        )
            .setConstraints(networkConstraint)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                DEFAULT_WORK_INTERVAL_IN_MINUTES,
                TimeUnit.MINUTES
            )
            .build()



        workManager.enqueueUniquePeriodicWork(
            SYNC_WORKER_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            locationWork
        )

        workManager.enqueueUniquePeriodicWork(
            MIN_PRICE_WORKER,
            ExistingPeriodicWorkPolicy.UPDATE,
            priceWork
        )
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
    }

}