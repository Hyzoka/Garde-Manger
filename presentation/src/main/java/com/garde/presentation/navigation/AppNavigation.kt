package com.garde.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.garde.presentation.screen.HomeScreen
import com.garde.presentation.screen.TextRecognitionScreen
import com.garde.presentation.screen.addproduct.AddProductScreen

internal sealed class Screen(val route: String) {
    object Home : Screen("home")
    object TextRecognition : Screen("text_recognition")
    object BarcodeScanning : Screen("barcode_scanning")

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.TextRecognition.route) {
            TextRecognitionScreen(navController)
        }
        composable(route = Screen.BarcodeScanning.route) {
            AddProductScreen(navController)
        }
    }
}