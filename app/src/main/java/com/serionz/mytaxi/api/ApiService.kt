package com.serionz.mytaxi.api

import com.serionz.mytaxi.api.responses.CabsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by johns on 25/01/2019.
 */

interface ApiService {

    @GET("/")
    fun getCabsList(
        @Query("p1Lat") p1Lat: Double,
        @Query("p1Lon") p1Lon: Double,
        @Query("p2Lat") p2Lat: Double,
        @Query("p2Lon") p2Lon: Double
    ): Call<CabsResponse>

}
