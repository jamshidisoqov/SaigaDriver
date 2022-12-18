package uz.gita.saiga_driver.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.saiga_driver.directions.SplashScreenDirection
import uz.gita.saiga_driver.directions.VerifyScreenDirection
import uz.gita.saiga_driver.directions.impl.SplashScreenDirectionImpl
import uz.gita.saiga_driver.directions.impl.VerifyScreenDirectionImpl

// Created by Jamshid Isoqov on 12/12/2022
@Module
@InstallIn(ViewModelComponent::class)
interface DirectionsModule {

    @Binds
    fun bindSplashDirections(impl: SplashScreenDirectionImpl): SplashScreenDirection

    @Binds
    fun bindVerifyDirections(impl: VerifyScreenDirectionImpl): VerifyScreenDirection

}