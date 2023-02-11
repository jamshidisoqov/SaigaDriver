package uz.gita.saiga_driver.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import uz.gita.saiga_driver.BuildConfig

// Created by Jamshid Isoqov on 12/12/2022
@HiltAndroidApp
class App:Application() {

    companion object {
        lateinit var instance: App
    }
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        instance = this
    }

}