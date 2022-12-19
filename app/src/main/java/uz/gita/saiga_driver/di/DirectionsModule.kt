package uz.gita.saiga_driver.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.saiga_driver.directions.*
import uz.gita.saiga_driver.directions.impl.*

// Created by Jamshid Isoqov on 12/12/2022
@Module
@InstallIn(ViewModelComponent::class)
interface DirectionsModule {

    @Binds
    fun bindSplashDirections(impl: SplashScreenDirectionImpl): SplashScreenDirection

    @Binds
    fun bindVerifyDirections(impl: VerifyScreenDirectionImpl): VerifyScreenDirection

    @Binds
    fun bindLoginDirections(impl: LoginScreenDirectionImpl): LoginScreenDirection

    @Binds
    fun bindRegisterDirections(impl: RegisterScreenDirectionImpl): RegisterScreenDirection

    @Binds
    fun bindPermissionDirections(impl: PermissionDirectionImpl): PermissionDirection

    @Binds
    fun bindMainDirections(impl: MainScreenDirectionImpl): MainScreenDirection

    @Binds
    fun bindHomeDirections(impl: HomeScreenDirectionImpl): HomeScreenDirection

}