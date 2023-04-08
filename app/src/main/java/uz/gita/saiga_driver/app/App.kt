package uz.gita.saiga_driver.app

import android.app.Application
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import uz.gita.saiga_driver.BuildConfig
import uz.gita.saiga_driver.app.work.LocationWork
import java.util.concurrent.TimeUnit

// Created by Jamshid Isoqov on 12/12/2022


const val DEFAULT_WORK_INTERVAL_IN_MINUTES = 15L
const val SYNC_WORKER_NAME = "SYNC_WORKER"

@HiltAndroidApp
class App : Application() {

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

        workManager.enqueueUniquePeriodicWork(
            SYNC_WORKER_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            locationWork
        )
    }

}