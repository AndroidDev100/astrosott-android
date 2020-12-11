package com.astro.sott.networking.retrofit;

import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.BuildConfig;
import com.astro.sott.utils.constants.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestConfig {
    private static Retrofit retrofit = null;
    private static Retrofit otpRetrofit = null;
    private static Retrofit verifyOtpRetrofit = null;
    private static Retrofit dtvRetrofit = null;

    private static String OLD_BASE_URL = null;
    public static Retrofit getClient(String BASE_URL) {
        if (retrofit == null || !OLD_BASE_URL.equals(BASE_URL)) {
            OLD_BASE_URL = BASE_URL;
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    private static OkHttpClient getHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        httpClient.addInterceptor(logging);

        httpClient.connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        return httpClient.build();
    }

    public static Retrofit getDTVClient(String BASE_URL) {
        if (dtvRetrofit == null || !OLD_BASE_URL.equals(BASE_URL)) {
            OLD_BASE_URL = BASE_URL;
            dtvRetrofit = new Retrofit.Builder()
                    .baseUrl("https://"+BASE_URL)
                    .client(getDTVHttpClient(BASE_URL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return dtvRetrofit;
    }


    private static OkHttpClient getDTVHttpClient(String baseUrl) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        httpClient.addInterceptor(logging);

        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(baseUrl, AppConstants.CERT_SEND_PUBLIC_KEY)
                .build();

        httpClient.connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        return httpClient.certificatePinner(certificatePinner).build();
    }

    public static Retrofit getOTPClient(String BASE_URL) {
        if (otpRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add(AppConstants.CERT_SEND_BASE_URL, AppConstants.CERT_SEND_PUBLIC_KEY)
                    .build();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-device", AppLevelConstants.DEVICE)
                        .addHeader("x-platform", AppConstants.Platform);
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.certificatePinner(certificatePinner).build();

            otpRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return otpRetrofit;
    }
    public static Retrofit getVerifyOTPClient(String BASE_URL) {
        if (verifyOtpRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add(AppConstants.CERT_VERIFY_BASE_URL, AppConstants.CERT_VERTIFY_PUBLIC_KEY)
                    .build();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-device", AppLevelConstants.DEVICE)
                        .addHeader("x-platform", AppConstants.Platform);
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.certificatePinner(certificatePinner).build();

            verifyOtpRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return verifyOtpRetrofit;
    }
}

