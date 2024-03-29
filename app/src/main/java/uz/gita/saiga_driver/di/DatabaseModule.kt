package uz.gita.saiga_driver.di

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import uz.gita.saiga_driver.data.local.prefs.MySharedPref
import uz.gita.saiga_driver.data.remote.api.AuthApi
import uz.gita.saiga_driver.data.remote.api.DirectionsApi
import uz.gita.saiga_driver.data.remote.api.NominationApi
import uz.gita.saiga_driver.data.remote.api.OrderApi
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

// Created by Jamshid Isoqov on 12/12/2022
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val SHARED_NAME: String = "app_data"
    private const val SHARED_MODE: Int = Context.MODE_PRIVATE
    val unauthorizedLiveData: MutableLiveData<Unit> = MutableLiveData()


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
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(20L, TimeUnit.SECONDS)
            .writeTimeout(20L, TimeUnit.SECONDS)
            .connectTimeout(20L, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                if (mySharedPref.token.isNotEmpty())
                    requestBuilder.addHeader("Authorization", "Bearer ${mySharedPref.token}")
                chain.proceed(requestBuilder.build())
            }
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                if (response.code == 403) {
                    unauthorizedLiveData.postValue(Unit)
                }
                response
            }
            .build()

    @[Provides Singleton]
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
        mySharedPref: MySharedPref
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(mySharedPref.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @[Provides Singleton Named("nomination_api")]
    fun provideNominationRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create(gson))
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
    fun provideStomp(okHttpClient: OkHttpClient, mySharedPref: MySharedPref): StompClient {
        return Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            mySharedPref.socketBaseUrl,
            emptyMap(),
            okHttpClient
        )
    }

    @[Provides Singleton]
    fun provideNominationApi(@Named("nomination_api") retrofit: Retrofit): NominationApi =
        retrofit.create(NominationApi::class.java)

}