package com.garde.manger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.garde.core.DestinationRoute
import com.garde.manger.ui.theme.GardeMangerTheme
import com.test.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GardeMangerTheme { // Ton thème global
                val navController = rememberNavController()
                Scaffold { padding ->
                    AppNavGraph(
                        navController = navController,
                        startDestination = DestinationRoute.PRODUCT_SCREEN,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}