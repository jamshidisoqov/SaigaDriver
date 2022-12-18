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
import javax.inject.Singleton

// Created by Jamshid Isoqov on 12/12/2022
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val SHARED_NAME: String = "app_data"
    private const val SHARED_MODE: Int = Context.MODE_PRIVATE
    private const val BASE_URL: String = "https://78e8-37-110-214-103.eu.ngrok.io/"
    private const val SOCKET_URL: String = "ws://78e8-37-110-214-103.eu.ngrok.io/saiga-websocket"

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
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                if (mySharedPref.token.isNotEmpty())
                    requestBuilder.addHeader("Authorization", "Bearer ${mySharedPref.token}")
                else if (mySharedPref.verifyToken.isNotEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer ${mySharedPref.verifyToken}")
                }
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
    fun provideStomp(): StompClient {
        return Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL)
            .withClientHeartbeat(5000)
            .withServerHeartbeat(5000)
    }

}