package com.serionz.mytaxi.api

import com.serionz.mytaxi.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {
    private var retrofitApi: Retrofit? = null

    private val client = OkHttpClient().newBuilder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
        .build()

    private fun createRetrofitClient(baseURL: String, client: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())

    fun getApiClient(): Retrofit? {
        val baseURL = "https://fake-poi-api.mytaxi.com"
        if (retrofitApi == null) {
            retrofitApi = createRetrofitClient(baseURL, client)
                .build()
        }
        return retrofitApi
    }
}
