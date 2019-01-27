package com.serionz.mytaxi.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.serionz.mytaxi.api.Repository


class CustomViewModelFactory(private val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CabsListFragmentViewModel::class.java)) {
            return CabsListFragmentViewModel(repo) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found!")
        }
    }
}
