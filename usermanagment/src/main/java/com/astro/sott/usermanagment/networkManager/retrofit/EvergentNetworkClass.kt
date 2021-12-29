package com.astro.sott.usermanagment.networkManager.retrofit

import com.astro.sott.usermanagment.BuildConfig
import com.astro.sott.usermanagment.EvergentBaseClient.EvergentBaseClient
import com.astro.sott.usermanagment.EvergentBaseClient.EvergentBaseConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class EvergentNetworkClass {
    private var retrofit: Retrofit? = null

    val client: Retrofit?
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY)
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor { chain ->
                    chain.proceed(
                        chain.request()
                            .newBuilder()
                            .header("User-Agent", System.getProperty("http.agent")+"-AMB-"+EvergentBaseClient.getAppVersion())
                            .build()
                    )
                }
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor) //.addNetworkInterceptor(networkInterceptor)
                .build()
            if (retrofit == null) {
                if (EvergentBaseConfiguration.instance.clients != null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(EvergentBaseConfiguration.instance.clients?.getBaseUrl())
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build()
                }
            }
            return retrofit
        }
}