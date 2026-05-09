package com.example.yatrimitra.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.*

import com.example.yatrimitra.screens.AuthScreen
import com.example.yatrimitra.screens.RouteSelectionScreen
import com.example.yatrimitra.screens.TrackingScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "auth" // 🔥 Start from Auth screen
    ) {

        // 🔹 Auth Screen (Phone + OTP + City)
        composable("auth") {
            AuthScreen(navController)
        }

        // 🔹 Route Selection Screen
        composable("routes") {
            RouteSelectionScreen(navController)
        }

        // 🔹 Tracking Screen
        composable(
            route = "tracking/{routeName}",
            arguments = listOf(
                navArgument("routeName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val routeName = Uri.decode(
                backStackEntry.arguments?.getString("routeName") ?: ""
            )

            TrackingScreen(routeName)
        }
    }
}