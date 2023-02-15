package uz.gita.saiga_driver.di

import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.data.remote.api.AuthApi
import uz.gita.saiga_driver.data.remote.api.DirectionsApi
import uz.gita.saiga_driver.data.remote.api.OrderApi
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// Created by Jamshid Isoqov on 12/12/2022
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val SHARED_NAME: String = "app_data"
    private const val SHARED_MODE: Int = Context.MODE_PRIVATE
    private const val BASE_URL: String = "http://157.230.38.77:5001"
    private const val SOCKET_BASE_URL: String = "ws://157.230.38.77:5000"


    @[Provides Singleton]
    fun provideSharedPreferences(@ApplicationContext ctx: Context): SharedPreferences {
        return ctx.getSharedPreferences(SHARED_NAME, SHARED_MODE)
    }

    @[Provides Singleton]
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @[Provides Singleton]
    fun provideClient(
        @ApplicationContext ctx: Context,
        mySharedPref: MySharedPref
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(ctx).build())
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .connectTimeout(10L, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                if (mySharedPref.token.isNotEmpty())
                    requestBuilder.addHeader("Authorization", "Bearer ${mySharedPref.token}")
                chain.proceed(requestBuilder.build())
            }
            .build()

    @[Provides Singleton]
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @[Provides Singleton]
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @[Provides Singleton]
    fun provideOrderApi(retrofit: Retrofit): OrderApi =
        retrofit.create(OrderApi::class.java)

    @[Provides Singleton]
    fun provideDirectionsApi(retrofit: Retrofit): DirectionsApi =
        retrofit.create(DirectionsApi::class.java)

    @[Provides Singleton]
    fun provideStomp(mySharedPref: MySharedPref, okHttpClient: OkHttpClient): StompClient {
        val headers = mapOf("Authorization" to "Bearer ${mySharedPref.token}")
        return Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_BASE_URL, headers, okHttpClient)
    }


}