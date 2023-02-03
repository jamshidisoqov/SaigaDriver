package uz.gita.saiga_driver.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.saiga_driver.domain.repository.AuthRepository
import uz.gita.saiga_driver.domain.repository.DirectionsRepository
import uz.gita.saiga_driver.domain.repository.MapRepository
import uz.gita.saiga_driver.domain.repository.OrderRepository
import uz.gita.saiga_driver.domain.repository.impl.AuthRepositoryImpl
import uz.gita.saiga_driver.domain.repository.impl.DirectionsRepositoryImpl
import uz.gita.saiga_driver.domain.repository.impl.MapRepositoryImpl
import uz.gita.saiga_driver.domain.repository.impl.OrderRepositoryImpl
import javax.inject.Singleton

// Created by Jamshid Isoqov on 12/12/2022
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @[Binds Singleton]
    fun bindMapRepository(impl: MapRepositoryImpl): MapRepository

    @[Binds Singleton]
    fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository

    @[Binds Singleton]
    fun bindDirectionRepository(impl: DirectionsRepositoryImpl): DirectionsRepository

}