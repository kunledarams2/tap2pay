package com.e.bloctap2pay.nfc



//import com.google.android.datatransport.backend.cct.BuildConfig

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.e.bloctap2pay.BuildConfig
import com.e.bloctap2pay.nfc.utils.PrefsUtils
import com.e.bloctap2pay.nfc.utils.PrefsValueHelper


import com.google.gson.Gson

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideBlocAPIService(
        @Named("BlocApiService") client: Lazy<OkHttpClient>,
        gson: Gson,
        prefsValueHelper: PrefsValueHelper
    ): BlocApiService {
        val baseUrl = if (prefsValueHelper.initParams!!.appEnvironment =="test")  "https://dev.one.blochq.io/v1" else "https://api.blochq.io/v1"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(provideOkHttpClient(prefsValueHelper))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

            .create(BlocApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesApplicationContext(app: Application): Context = app.applicationContext


    private fun provideOkHttpClient(
        prefsValueHelper: PrefsValueHelper
    ): OkHttpClient {
        val b = OkHttpClient.Builder()
        b.connectTimeout(60, TimeUnit.SECONDS)
        b.readTimeout(60, TimeUnit.SECONDS)
        b.writeTimeout(60, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        b.addInterceptor(interceptor)
        b.addInterceptor{ chain -> val request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${prefsValueHelper.initParams!!.secretKey}").build()
            chain.proceed(request)

        }
        b.addInterceptor{ chain -> val request = chain.request().newBuilder().addHeader("app-environment", prefsValueHelper.initParams!!.appEnvironment).build()
            chain.proceed(request)

        }
        //adds logs to logcat
        return b.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)


    @Provides
    @Singleton
    fun providePrefsUtils(prefs: SharedPreferences, gson: Gson): PrefsUtils =
        PrefsUtils(prefs, gson)

    @Provides
    @Singleton
    fun provideGlobalSharedPreference(app: Application): SharedPreferences =
        app.getSharedPreferences("global_shared_prefs", Context.MODE_PRIVATE)


}