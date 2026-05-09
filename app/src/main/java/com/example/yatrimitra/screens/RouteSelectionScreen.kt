package com.example.yatrimitra.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.yatrimitra.R

@Composable
fun RouteSelectionScreen(navController: NavController) {

    val routes = listOf(
        "Main Stand → Railway Station",
        "Bus Stop → College",
        "Market → Hospital"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // 🖤 Dark background
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Yatri-Mitra",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(text = "Instant Ride Arrival App ",
            color = Color.LightGray)

        Text(text="(Shared Mobility)",
            color = Color.LightGray)

        Spacer(modifier = Modifier.height(7.dp))

        // 🚖 Small Auto Image (optional logo style)
        Image(
            painter = painterResource(id = R.drawable.auto),
            contentDescription = "Auto Icon",
            modifier = Modifier
                .size(140.dp)
                .alpha(0.9f)
        )

        // 🚍 Header
        Text(
            text = "Select Your Route",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Track your shared auto in real-time",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(10.dp))

        routes.forEach { route ->

            Card(
                onClick = {
                    val encodedRoute = Uri.encode(route)
                    navController.navigate("tracking/$encodedRoute")
                },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White // dark card
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 🚖 Icon with yellow background
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFFC107) // yellow
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBus,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {

                        Text(
                            text = route,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Tap to track live",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    // ➡️ Arrow
                    Text(
                        text = "➜",
                        color = Color.Gray,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}