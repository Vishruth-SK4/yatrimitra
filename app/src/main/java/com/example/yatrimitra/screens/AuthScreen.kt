package com.example.yatrimitra.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.yatrimitra.R

@Composable
fun AuthScreen(navController: NavController) {

    var step by remember { mutableStateOf(1) }
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    val cities = listOf("Bangalore", "Mysore", "Mangalore")

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4FACFE),
            Color(0xFF00F2FE)
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // 🚖 Background Image
        Image(
            painter = painterResource(id = R.drawable.auto),
            contentDescription = "Auto Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🌈 Gradient Overlay (for readability)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .alpha(0.2f)  //✅ apply transparency here
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            // 🚍 App Title
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

            Spacer(modifier = Modifier.height(10.dp))

            // 🚖 Small Auto Image (optional logo style)
            Image(
                painter = painterResource(id = R.drawable.auto),
                contentDescription = "Auto Icon",
                modifier = Modifier
                    .size(170.dp)
                    .alpha(0.9f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 📦 Card Container
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    if (step == 1) {

                        Text("Enter Phone Number")

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (phone.length >= 10) step = 2
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            shape = RoundedCornerShape(14.dp),
                            enabled = phone.length >= 10,
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 6.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC107), // 🚖 Yellow
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = "Send OTP",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    else if (step == 2) {

                        Text("Verify OTP")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Sent to $phone", color = Color.Gray)

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = otp,
                            onValueChange = { otp = it },
                            label = { Text("Enter OTP") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (otp.isNotEmpty()) step = 3
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            shape = RoundedCornerShape(14.dp),
                            enabled = otp.isNotEmpty(),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 6.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC107), // 🚖 Yellow theme
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = "Verify OTP",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    else {

                        Text(
                            text = "Select Your City",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        cities.forEach { city ->
                            Card(
                                onClick = {
                                    navController.navigate("routes")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(6.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF5F5F5)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(18.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = city,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "➜",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}