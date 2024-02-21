package com.e.bloctap2pay.nfc.network

import android.content.Context
import com.e.bloctap2pay.nfc.utils.PrefsValueHelper
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object RetrofitInstance {


    private fun createRetrofit(context: Context): Retrofit {
        val baseUrl = "https://dev.one.blochq.io/v1/"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory()) // Use the coroutine adapter
            .client(provideOkHttpClient(PrefsValueHelper(context)))
            .build()
    }

    fun blocApiService(context: Context): BlocApiService {
        return createRetrofit(context).create()
    }

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
        b.addInterceptor{ chain -> val request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${prefsValueHelper.initParams!!.publicKey}").build()
            chain.proceed(request)

        }

        //adds logs to logcat
        return b.build()
    }
}