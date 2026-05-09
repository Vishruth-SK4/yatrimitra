package com.example.yatrimitra.model

import org.osmdroid.util.GeoPoint

data class VehicleState(

    val id: Int,

    val name: String,

    val position: GeoPoint,
    val driverName: String,
    val numberPlate: String,
    val contactNumber: String,

    val progress: Float,
    val eta: Int,
    val routeDistanceKm: Float
)