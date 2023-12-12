package com.example.planteria.network

import com.example.planteria.BuildConfig
import com.example.planteria.PlanteriaApplication
import com.example.planteria.utils.PrefHelper
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {


    fun getInstance(): Retrofit {
        val mHttpInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(mHttpInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                    .addHeader("x-api-key", BuildConfig.XAPI_KEY)
                    .addHeader("Authorization",
                        PlanteriaApplication.prefHelper.getString(PrefHelper.TOKEN).toString()
                    )
                chain.proceed(request.build())
            }
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .client(mOkHttpClient)
            .build()

        return retrofit
    }
}