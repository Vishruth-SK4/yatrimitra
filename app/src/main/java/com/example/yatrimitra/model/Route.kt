package com.example.yatrimitra.model

data class Route(
    val id: String,
    val name: String,
    val startName: String,
    val endName: String,
    val startLat: Double,
    val startLng: Double,
    val endLat: Double,
    val endLng: Double,
    val distance: Double
)