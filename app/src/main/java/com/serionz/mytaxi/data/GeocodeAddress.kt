package com.serionz.mytaxi.data

import com.google.gson.annotations.SerializedName


data class GeocodeAddress(@SerializedName("formatted_address") val formattedAddress: String?)
