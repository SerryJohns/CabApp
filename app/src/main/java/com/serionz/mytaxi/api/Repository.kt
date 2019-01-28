package com.serionz.mytaxi.api

import com.serionz.mytaxi.api.responses.CabsResponse
import com.serionz.mytaxi.data.Bounds


class Repository {
    private val apiService = ApiClient().getApiClient()?.create(ApiService::class.java)

    fun fetchCabs(bounds: Bounds): CabsResponse? {
        val call = apiService?.getCabsList(bounds.p1Lat, bounds.p1Lon, bounds.p2Lat, bounds.p2Lon)
        return call?.execute()?.body()
    }
}
