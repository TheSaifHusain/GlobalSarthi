package com.global.sarthi.di

import android.content.Context
import android.telephony.TelephonyManager
import com.global.sarthi.db.DataStoreManager
import com.global.sarthi.network.ApiRepository
import com.global.sarthi.network.AppApi
import com.global.sarthi.utils.Utils
import com.global.sarthi.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModules {

    private val BASE_URL = "http://110.227.200.246:8080/"

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(20000, TimeUnit.MILLISECONDS) // Increase the read timeout to 60 seconds
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun providesAppAPi(retrofit: Retrofit): AppApi {
        return retrofit.create(AppApi::class.java)
    }

    @Provides
    @Singleton
    fun providesViewModel(@ApplicationContext context: Context, apiRepository: ApiRepository,utils: Utils, dataStoreManager: DataStoreManager): MainViewModel
    = MainViewModel(context, apiRepository, utils,dataStoreManager)


    @Provides
    @Singleton
    fun provideTelephonyManager(@ApplicationContext context: Context): TelephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope =
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context) : DataStoreManager = DataStoreManager(context = context)

    @Provides
    @Singleton
    fun providesUtils(
        @ApplicationContext context: Context,
        telephonyManager: TelephonyManager,
        scope: CoroutineScope,
        dataStoreManager: DataStoreManager
    ) : Utils = Utils(context,telephonyManager,scope,dataStoreManager)

}