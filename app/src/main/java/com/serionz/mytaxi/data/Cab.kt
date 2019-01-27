package com.serionz.mytaxi.data


data class Cab(val id: Int, val coordinate: Coordinate, val fleetType: FleetType, val heading: Double, var address: String?)