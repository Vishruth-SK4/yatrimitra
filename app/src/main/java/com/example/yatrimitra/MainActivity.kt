package com.example.yatrimitra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.yatrimitra.navigation.AppNavigation
import com.example.yatrimitra.ui.theme.YatrimitraTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            YatrimitraTheme {
                AppNavigation()
            }
        }
    }
}