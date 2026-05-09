package com.example.yatrimitra.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yatrimitra.R
import com.example.yatrimitra.screens.components.OSMMapView
import com.example.yatrimitra.viewmodel.TrackingViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(routeName: String) {

    val viewModel: TrackingViewModel = viewModel()

    val isRunning by viewModel.isRunning.collectAsState()
    val vehicles by viewModel.vehicles.collectAsState()
    val routes by viewModel.multiRoutes.collectAsState()
    val eta by viewModel.eta.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            // 🚍 Top Bar
            Text(
                text = "Yatri-Mitra",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(text = "Instant Ride Arrival App ",
                color = Color.LightGray)

            Text(text="(Shared Mobility)",
                color = Color.LightGray)

            Spacer(modifier = Modifier.height(3.dp))

            // 🚖 Header Image
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.auto),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {

                    Text(
                        text = "Live Tracking",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )

                    Text(
                        text = routeName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(9.dp))

            // 🚖 Start Tracking Button
            Button(
                onClick = { viewModel.toggleTracking() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = if (isRunning) "Stop Tracking" else "Start Tracking",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 🗺️ Map Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                shape = RoundedCornerShape(25.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                OSMMapView(
                    routes = routes,
                    vehicles = vehicles
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 🚘 Vehicle List Title
            Text(
                text = "Available Vehicles",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(3.dp))

            val bestVehicle =
                vehicles.minByOrNull { it.eta }

            vehicles.forEach { vehicle ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // 🚖 Auto Image
                        Image(
                            painter = painterResource(id = R.drawable.auto),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = vehicle.name,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(1.dp))

                            val minutes = vehicle.eta / 60
                            val seconds = vehicle.eta % 60
                            Text(
                                text = "ETA: ${minutes}m ${seconds}s",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )

                            val displayProgress =
                                if (!viewModel.isReversing()) {
                                    vehicle.progress
                                } else {
                                    1f - vehicle.progress
                                }
                            Text(
                                text = "Progress: ${(displayProgress * 100).toInt()}%",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                            if (vehicle.id == bestVehicle?.id) {
                                Text(
                                    text = "⭐ Best Recommended",
                                    color = Color.Green,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))
                        var showDialog by remember { mutableStateOf(false) }

                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier.padding(top = 2.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC107),
                                contentColor = Color.Black
                            )
                        ) {
                            Text("View Details")
                        }
                        if (showDialog) {

                            AlertDialog(
                                onDismissRequest = { showDialog = false },

                                confirmButton = {
                                    TextButton(
                                        onClick = { showDialog = false }
                                    ) {
                                        Text("Close")
                                    }
                                },

                                title = {
                                    Text(text = vehicle.name)
                                },

                                text = {

                                    Column {

                                        Text("Driver Name: ${vehicle.driverName}")

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text("Vehicle Number: ${vehicle.numberPlate}")

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text("Contact Number: ${vehicle.contactNumber}")

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "Route Distance: ${"%.1f".format(vehicle.routeDistanceKm)} km"
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        val remainingKm =
                                            vehicle.routeDistanceKm * (1f - vehicle.progress)

                                        Text(
                                            text = "Remaining: ${"%.1f".format(remainingKm)} km",
                                        )

                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.width(3.dp))

                        // 🟢 Status Dot
                        Text(
                            text = if (isRunning) "🟢" else "🔴"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // 📍 Status
            Text(
                text = "Status: ${if (isRunning) "Running" else "Paused"}",
                color = if (isRunning) Color.Green else Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}