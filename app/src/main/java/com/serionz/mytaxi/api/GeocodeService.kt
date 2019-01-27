package com.serionz.mytaxi.api

import com.google.android.gms.maps.model.LatLng
import com.serionz.mytaxi.BuildConfig
import com.serionz.mytaxi.api.responses.GeocodeAddressResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by johns on 26/01/2019.
 */

interface GeocodeService {

    @GET("geocode/json")
    fun getGeocodeAddress(
        @Query("latlng") latLng: LatLng,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Call<GeocodeAddressResponse>

}
