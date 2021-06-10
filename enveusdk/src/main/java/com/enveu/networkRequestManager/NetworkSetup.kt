package com.enveu.networkRequestManager

import com.enveu.BaseClient.BaseConfiguration
import com.enveu.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkSetup {
    private var retrofitApi: Retrofit? = null
    private var userMngmtRetrofit: Retrofit? = null
    private lateinit var subscriptionManagementRetrofit: Retrofit

    val client: Retrofit?
        get() {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            val okHttpClient = OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build()

            if (retrofitApi == null) {
                if (BaseConfiguration.instance.clients != null) {
                    retrofitApi = Retrofit.Builder()
                            .baseUrl(BaseConfiguration.instance.clients?.getBaseUrl())
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build()
                }
            }
            return retrofitApi
        }

    val userMngmtClient: Retrofit?
        get() {

            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG)
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            else
                loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    // Request customization: add request headers
                    val requestBuilder = BaseConfiguration.instance.clients?.getOVPApiKey()?.let {
                        original.newBuilder()
                                .addHeader("x-api-key", it)
                    }
                    val request = requestBuilder!!.build()
                    return chain.proceed(request)
                }
            })
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor)

            val client = httpClient.build()
            if (BaseConfiguration.instance.clients != null) {
                userMngmtRetrofit = Retrofit.Builder()
                        .baseUrl(BaseConfiguration.instance.clients?.getUserMngmntBaseUrl())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(client)
                        .build()
            }

            return userMngmtRetrofit
        }

    fun subscriptionClient(token: String): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG)
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()

                // Request customization: add request headers

                val requestBuilder = BaseConfiguration.instance.clients?.getOVPApiKey()?.let {
                    original.newBuilder()
                            .addHeader("x-api-key", it)
                            .addHeader("x-auth", token)
                }
                val request = requestBuilder!!.build()

                return chain.proceed(request)
            }

        })
        httpClient.readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .cache(null)
                .addInterceptor(loggingInterceptor)

        val client = httpClient.build()
        if (BaseConfiguration.instance.clients != null) {
            subscriptionManagementRetrofit = Retrofit.Builder()
                    .baseUrl(BaseConfiguration.instance.clients?.getUserMngmntBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()
        }
        return subscriptionManagementRetrofit
    }
}