package uz.gita.saiga_driver.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.saiga_driver.domain.repository.directions.DirectionsRepository
import javax.inject.Singleton

// Created by Jamshid Isoqov on 12/12/2022
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {


    @[Binds Singleton]
    fun bindDirectionRepository(impl: DirectionsRepository):DirectionsRepository

}