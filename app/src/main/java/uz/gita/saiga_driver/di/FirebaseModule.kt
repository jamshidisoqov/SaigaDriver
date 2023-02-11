package uz.gita.saiga_driver.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Created by Jamshid Isoqov on 1/15/2023
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @[Provides Singleton]
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

}