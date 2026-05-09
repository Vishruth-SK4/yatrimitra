package com.example.yatrimitra.network

data class OsrmResponse(
    val routes: List<Route>
)

data class Route(
    val geometry: String,
    val distance: Double

)