package com.serionz.mytaxi.api

import com.google.android.gms.maps.model.LatLng
import com.serionz.mytaxi.api.responses.CabsResponse
import com.serionz.mytaxi.api.responses.GeocodeAddressResponse
import com.serionz.mytaxi.data.Bounds


class Repository {
    private val apiService = ApiClient().getApiClient()?.create(ApiService::class.java)
    private val geocodeService = ApiClient().getGeocodeClient()?.create(GeocodeService::class.java)

    fun fetchCabs(bounds: Bounds): CabsResponse? {
        val call = apiService?.getCabsList(bounds.p1Lat, bounds.p1Lon, bounds.p2Lat, bounds.p2Lon)
        return call?.execute()?.body()
    }

    fun getGeocodeAddress(latLng: LatLng): GeocodeAddressResponse? {
        val call = geocodeService?.getGeocodeAddress(latLng)
        return call?.execute()?.body()
    }
}
