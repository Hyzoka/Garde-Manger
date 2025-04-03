package com.garde.feature.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.garde.feature.screen.addproduct.AddProductScreen
import com.garde.feature.screen.products.ProductListScreen

internal sealed class Screen(val route: String) {
    object ProductList : Screen("product_list")
    object AddNewProduct : Screen("add_new_product")

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = Screen.ProductList.route
        ) {
            composable(route = Screen.ProductList.route) {
                ProductListScreen(navController = navController)
            }
            composable(route = Screen.AddNewProduct.route, enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }, exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }) {
                AddProductScreen(navController = navController)
            }
        }
    }
}
