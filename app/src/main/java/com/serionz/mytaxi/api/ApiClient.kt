package com.serionz.mytaxi.api

import com.google.android.gms.maps.model.LatLng
import com.serionz.mytaxi.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


class ApiClient {
    private var retrofitApi: Retrofit? = null
    private var retrofitGeocode: Retrofit? = null

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

    fun getGeocodeClient(): Retrofit? {
        val baseURL = "https://maps.googleapis.com/maps/api/"
        if (retrofitGeocode == null) {
            retrofitGeocode = createRetrofitClient(baseURL, client)
                .addConverterFactory(object : Converter.Factory() {
                    override fun stringConverter(
                        type: Type,
                        annotations: Array<Annotation>,
                        retrofit: Retrofit
                    ): Converter<*, String>? {
                        if (type != LatLng::class.java) {
                            return super.stringConverter(type, annotations, retrofit)
                        }
                        return Converter { latLng: LatLng -> "${latLng.latitude},${latLng.longitude}" }
                    }
                })
                .build()
        }
        return retrofitGeocode
    }
}
