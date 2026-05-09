package com.example.yatrimitra.screens.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.util.GeoPoint
import com.example.yatrimitra.model.VehicleState
import com.example.yatrimitra.R

@Composable
fun OSMMapView(
    routes: List<List<GeoPoint>>,
    vehicles: List<VehicleState>
){

    val context = LocalContext.current

    val mapView = remember { MapView(context) }

    AndroidView(
        factory = {

            Configuration.getInstance().load(
                context,
                context.getSharedPreferences("osm", Context.MODE_PRIVATE)
            )

            mapView.apply {
                setMultiTouchControls(true)
                controller.setZoom(15.0)
            }
        },

        update = { map ->

            map.overlays.clear()

            if (routes.isNotEmpty()) {

                // 🛣️ Draw all routes
                routes.forEachIndexed { index, route ->

                    if (route.isEmpty()) return@forEachIndexed

                    // 🟢 Start markers
                    val startMarker = Marker(map)
                    startMarker.position = route.first()
                    startMarker.title = "Start ${index + 1}"
                    map.overlays.add(startMarker)

                    val polyline = Polyline()
                    polyline.setPoints(route)
                    map.overlays.add(polyline)
                }

                // 🔴 SINGLE COMMON DESTINATION
                val commonEndPoint = routes.first().last()

                val endMarker = Marker(map)
                endMarker.position = commonEndPoint
                endMarker.title = "Railway Station"
                map.overlays.add(endMarker)

                // 🚗 Vehicles
                vehicles.forEach { vehicle ->

                    val marker = Marker(map)
                    marker.position = vehicle.position
                    marker.title = vehicle.name

                    marker.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )

                    val drawable = context.getDrawable(R.drawable.auto)
                    val bitmap = (drawable as BitmapDrawable).bitmap

                    val scaledBitmap = Bitmap.createScaledBitmap(
                        bitmap, 80, 80, true
                    )

                    marker.icon =
                        BitmapDrawable(context.resources, scaledBitmap)

                    map.overlays.add(marker)
                }

                // Center camera
                if (vehicles.isNotEmpty()) {
                    map.controller.setCenter(vehicles.first().position)
                }

                map.invalidate()
            }
        },


        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    )
}
