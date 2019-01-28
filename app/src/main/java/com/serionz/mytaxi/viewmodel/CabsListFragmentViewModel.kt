package com.serionz.mytaxi.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.serionz.mytaxi.api.Repository
import com.serionz.mytaxi.data.Bounds
import com.serionz.mytaxi.data.Cab


class CabsListFragmentViewModel(private val repo: Repository) : ViewModel() {
    var cabsList = MutableLiveData<List<Cab>>()
    var selectedCab = MutableLiveData<Cab?>()
    val bounds = Bounds(53.694865, 9.757589, 53.394655, 10.099891)

    fun fetchCabs(bounds: Bounds, refresh: Boolean = false): List<Cab> {
        var result: List<Cab>? = mutableListOf()
        if (cabsList.value?.size == null || refresh) {
            result = repo.fetchCabs(bounds)?.poiList
        }
        return result ?: mutableListOf()
    }

    fun selectCab(cab: Cab?) {
        selectedCab.value = cab
    }
}