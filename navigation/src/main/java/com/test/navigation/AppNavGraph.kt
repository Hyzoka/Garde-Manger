package com.test.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.test.add_product.screen.AddProductScreen
import com.test.product.screen.ProductListScreen

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String, modifier: Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(DestinationRoute.PRODUCT_SCREEN) {
            ProductListScreen({
                navController.navigate(DestinationRoute.ADD_PRODUCT_SCREEN)
            })
        }
        composable(DestinationRoute.ADD_PRODUCT_SCREEN, enterTransition = {
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
            AddProductScreen(onPopBackStack = {
                navController.popBackStack()
            })
        }
    }
}
