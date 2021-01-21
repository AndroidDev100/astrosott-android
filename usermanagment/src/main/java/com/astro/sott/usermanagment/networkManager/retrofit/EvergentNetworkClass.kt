package com.astro.sott.usermanagment.networkManager.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class EvergentNetworkClass {
   private var retrofit: Retrofit? = null

    val client:Retrofit?
    get() {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY)
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor) //.addNetworkInterceptor(networkInterceptor)
                .build()
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl("https://rest-stag.evergent.com/astro/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
        }
        return retrofit
    }
}