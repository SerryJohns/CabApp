package com.serionz.mytaxi.data

/**
 * Created by johns on 23/01/2019.
 */

data class Cab(val id: Int, val coordinate: Coordinate, val fleetType: FleetType, val heading: Double, var address: String?)