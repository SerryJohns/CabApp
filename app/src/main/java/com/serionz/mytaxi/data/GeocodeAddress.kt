package com.serionz.mytaxi.data

import com.google.gson.annotations.SerializedName

/**
 * Created by johns on 25/01/2019.
 */

data class GeocodeAddress(@SerializedName("formatted_address") val formattedAddress: String?)
